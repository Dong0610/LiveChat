package dong.duan.livechat

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.duan.lib.library.OnPutImageListener
import dong.duan.lib.library.putImgToStorage
import dong.duan.lib.library.sharedPreferences
import dong.duan.lib.library.show_toast
import dong.duan.livechat.Model.ChatData
import dong.duan.livechat.Model.ChatUser
import dong.duan.livechat.Model.Message
import dong.duan.livechat.Model.UserData
import dong.duan.livechat.utility.Event
import dong.duan.livechat.utility.KEY_CHAT
import dong.duan.livechat.utility.KEY_USER
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class LCViewModel @Inject constructor() : ViewModel() {

    val isLongPress = mutableStateOf(false)

    @SuppressLint("MutableCollectionMutableState")
    val chats = mutableStateOf<MutableList<ChatData>>(mutableListOf())
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
    val storage: FirebaseStorage = Firebase.storage
    val database: FirebaseDatabase = Firebase.database

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    var userImg = mutableStateOf<String?>(null)

    val listMessage = mutableStateOf<MutableList<Message>>(mutableListOf())

    val inProcessChat = mutableStateOf(false)


    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun SignUp(name: String, email: String, passWord: String) {
        inProcess.value = true

        if (name.isNotEmpty() && email.isNotEmpty() && passWord.isNotEmpty()) {
            firestore.collection(KEY_USER).whereEqualTo("userEmail", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        auth.createUserWithEmailAndPassword(email, passWord)
                            .addOnCompleteListener { createAccountTask ->
                                if (createAccountTask.isSuccessful) {
                                    // Send confirmation email
                                    sendConfirmationEmail(email)

                                    show_toast("Sign up success")
                                    signIn.value = true
                                    createOrUpdateProfile(name, email, passWord)
                                } else {
                                    createAccountTask.exception?.let { exception ->
                                        handleException(exception, "Sign up failed")
                                    }
                                }
                            }
                    } else {
                        show_toast("Email already exists")
                        inProcess.value = false
                    }
                }
                .addOnFailureListener { exception ->
                    handleException(exception, "Error checking email existence")
                }
        } else {
            show_toast("Please enter all values")
            inProcess.value = false
        }
    }


    fun resetPassword(email: String) {
        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        show_toast("Password reset email sent to $email")
                    } else {
                        task.exception?.let { exception ->
                            handleException(exception, "Error sending password reset email")
                        }
                    }
                }
        } else {
            show_toast("Please enter your email address")
        }
    }

    private fun sendConfirmationEmail(email: String) {
        val user = auth.currentUser
        user?.let {
            it.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        show_toast("Confirmation email sent to $email")
                    } else {
                        task.exception?.let { exception ->
                            handleException(exception, "Error sending confirmation email")
                        }
                    }
                }
        }
    }


    fun currentChat(chatID: String, calback: (ChatData) -> Unit) {
        chats.value.toMutableList().find { it -> it.chatID == chatID }?.let { it1 -> calback(it1) }
    }

    fun createOrUpdateProfile(
        usname: String = "",
        email: String = "",
        passWord: String = "",
        imageUrl: String? = null
    ) {
        val uuID = auth.currentUser?.uid
        val userData = UserData(
            userID = uuID,
            userToken = "",
            regTime = Date().time.toString(),
            phoneNum = "",
            isAvailable = true,
            userName = usname,
            userEmail = email,
            password = passWord,
            imgUrl = imageUrl ?: userData.value?.imgUrl,
        )
        uuID.let {
            inProcess.value = true
            firestore.collection(
                KEY_USER
            )
                .document(it.toString()).get().addOnSuccessListener {
                    if (!it.exists()) {
                        firestore.collection(KEY_USER).add(userData)
                        inProcess.value = false
                        getUserData(uuID)
                        resetPassword(email)
                    } else {
                        firestore.collection(KEY_USER).document(uuID.toString()).set(userData)
                        inProcess.value = false
                        getUserData(uuID)
                    }
                }
                .addOnFailureListener {
                    show_toast(it.message.toString())
                    handleException(it, "Create User Fluaid")
                }
        }
    }

    private fun getUserData(uuID: String?) {
        inProcess.value = true
        signIn.value = true

        firestore.collection(KEY_USER)
            .document(uuID!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                }
                if (value!!.exists()) {
                    var users = value.toObject<UserData>()
                    userData.value = users
                    userImg.value = users?.imgUrl
                    inProcess.value = false
                    sharedPreferences.putBollean("KEY_SIGN", true)
                    PopulateChat()
                }
            }
    }


    fun handleException(exception: Exception, customMess: String = "") {
        Log.e("LiveChat", "Live Chat Exceotion: ", exception)
        exception.printStackTrace()
        val errorMess = exception.localizedMessage ?: ""
        val message = if (customMess.isNullOrEmpty()) errorMess else customMess
        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun SignIn(email: String, pass: String) {
        inProcess.value = true
        resetPassword(email)
//        firestore.collection(KEY_USER).whereEqualTo("userEmail", email)
//            .get().addOnSuccessListener { querySnapshot ->
//                if (querySnapshot.isEmpty) {
//                    show_toast("Account is not registered in the app")
//                    inProcess.value = false
//                } else {
//                    val userDocument = querySnapshot.documents[0]
//                    if (userDocument.getString("password") == pass) {
//                        auth.signInWithEmailAndPassword(email, pass)
//                            .addOnCompleteListener {
//                                signIn.value = true
//                                inProcess.value = false
//                                auth.currentUser?.uid.let {
//                                    getUserData(uuID = auth.currentUser?.uid)
//                                }
//                            }
//
//                        inProcess.value = true
//                    } else {
//                        show_toast("Invalid password")
//                        inProcess.value = false
//                    }
//                }
//            }
    }

    fun uploadImageToFirebase(uri: Uri?) {
        inProcess.value = true
        storage.getReference("ImageUser")
            .putImgToStorage(uri, object : OnPutImageListener {
                override fun onComplete(url: String) {
                    inProcess.value = false
                    userImg.value = url
                    auth.currentUser?.uid
                        .let { it ->
                            firestore.collection(KEY_USER).document(it.toString()).update(
                                mapOf("imgUrl" to url)
                            )

                        }

                }

                override fun onFailure(mess: String) {
                    inProcess.value = false
                }
            })
    }

    fun onAddChat(email: String) {
        if (email.isEmpty() or email.isDigitsOnly()) {
            show_toast("Email must be container digits only")
        } else {
            firestore.collection(KEY_CHAT)
                .where(
                    Filter.or(
                        Filter.and(
                            Filter.equalTo("reciveUser.email", email),
                            Filter.equalTo("sendUser.email", userData.value?.userEmail)
                        ),
                        Filter.and(
                            Filter.equalTo("reciveUser.email", userData.value?.userEmail),
                            Filter.equalTo("sendUser.email", email)
                        )
                    )
                )
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        firestore.collection(KEY_USER)
                            .whereEqualTo("userEmail", email)
                            .get().addOnSuccessListener {
                                if (it.isEmpty) {
                                    show_toast("Email not found")
                                } else {
                                    val chatParter = it.toObjects<UserData>()[0]
                                    val id = firestore.collection(KEY_CHAT).document().id
                                    val chat = ChatData(
                                        chatID = id,
                                        ChatUser(
                                            userData.value?.userID,
                                            userData.value?.userName,
                                            userData.value?.userEmail,
                                            userData.value?.imgUrl
                                        ),
                                        ChatUser(
                                            chatParter.userID,
                                            chatParter.userName,
                                            chatParter.userEmail,
                                            chatParter.imgUrl
                                        )
                                    )

                                    firestore.collection(KEY_CHAT).document(id).set(chat)
                                        .addOnSuccessListener { show_toast("Create chat success") }

                                }
                            }
                            .addOnFailureListener {
                                show_toast(it.message.toString())
                            }
                    } else {
                        show_toast("Chat already exit")
                    }
                }
        }
    }

    fun InitMessage(chatID: String) {
        database.getReference("CHATS_LIST")
            .child(chatID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()

                    for (messageSnapshot in snapshot.children) {
                        val message = messageSnapshot.getValue(Message::class.java)
                        message?.let {
                            messages.add(it)
                        }
                    }

                    listMessage.value = messages
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun PopulateChat() {
        inProcessChat.value = true
        firestore.collection(KEY_CHAT)
            .where(
                Filter.or(
                    Filter.equalTo("sendUser.userID", userData.value?.userID),
                    Filter.equalTo("reciveUser.userID", userData.value?.userID),
                )
            ).addSnapshotListener { value, error ->
                if (error == null) {
                    if (value != null) {
                        chats.value = value.documents.mapNotNull {
                            it.toObject<ChatData>()
                        }.toMutableList()
                        inProcessChat.value = false
                    }
                } else {
                    show_toast("Error is not null")
                }
            }
    }

    fun SendMessage(messages: String, chatData: ChatData?) {
        if (chatData == null) {
            show_toast("ChatData is null")
            return
        }
        val chatID = chatData.chatID
        if (chatID == null) {
            show_toast("ChatID is null")
            return
        }


        var receiverID = ""

        if (chatData.sendUser?.userID == userData.value?.userID.toString()) {
            receiverID = chatData.reciveUser.userID.toString()
        } else {
            receiverID = chatData.sendUser?.userID.toString()
        }


        val message = Message().apply {
            id = database.getReference("CHATS_LIST").child(chatID).push().key
            this.message = messages.trim()
            time = Date().time.toString()
            this.senderID = userData.value?.userID.toString()
            this.receiveID = receiverID
        }

        database.getReference("CHATS_LIST").child(chatID).push().setValue(message)


    }

    fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    show_toast("Sign in Success")
                }
            }
    }


}






















