package dong.duan.livechat

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.duan.lib.library.OnPutImageListener
import dong.duan.lib.library.putImgToStorage
import dong.duan.lib.library.sharedPreferences
import dong.duan.lib.library.show_toast
import dong.duan.livechat.utility.ChatData
import dong.duan.livechat.utility.ChatUser
import dong.duan.livechat.utility.Event
import dong.duan.livechat.utility.KEY_CHAT
import dong.duan.livechat.utility.KEY_USER
import dong.duan.livechat.utility.UserData
import javax.inject.Inject


@HiltViewModel
class LCViewModel @Inject constructor() : ViewModel() {
    val chats = mutableStateOf<MutableList<ChatData>>(mutableListOf())
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
    val storage: FirebaseStorage = FirebaseStorage.getInstance()

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    var userImg = mutableStateOf<String?>(null)

    val inProcessChat = mutableStateOf(false)


    init {
        var currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun SignUp(name: String, email: String, passWord: String) {
        inProcess.value = true

        if (!name.isNullOrEmpty() && !email.isNullOrEmpty() && !passWord.isNullOrEmpty()) {
            firestore.collection(KEY_USER).whereEqualTo("UserEmail", email)
                .get().addOnSuccessListener {
                    if (it.isEmpty) {
                        auth.createUserWithEmailAndPassword(email, passWord)
                            .addOnCompleteListener {

                                if (it.isSuccessful) {
                                    show_toast("Sign up success")
                                    signIn.value = true
                                    createOrUpdateProfile(name, email, passWord)
                                } else {
                                    it.exception?.let { it1 ->
                                        handleExceotion(
                                            it1,
                                            "Sign in flaid"
                                        )
                                    }
                                }
                            }
                    } else {
                        show_toast("Email is exit")
                        inProcess.value = false
                    }
                }

        } else {
            show_toast("Please enter all value")
            return
        }
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
            userName = usname ?: userData.value?.userName,
            userEmail = email ?: userData.value?.userEmail,
            password = passWord ?: userData.value?.password,
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
                    } else {
                        firestore.collection(KEY_USER).document(uuID.toString()).set(userData)
                        inProcess.value = false
                        getUserData(uuID)
                    }
                }
                .addOnFailureListener {
                    show_toast(it.message.toString())
                    handleExceotion(it, "Create User Fluaid")
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
                    handleExceotion(error)
                }
                if (value!!.exists()) {
                    var users = value.toObject<UserData>()
                    userData.value = users
                    userImg.value = users?.imgUrl
                    inProcess.value = false
                    sharedPreferences.putBollean("KEY_SIGN", true)
                }
            }
    }


    fun handleExceotion(exception: Exception, customMess: String = "") {
        Log.e("LiveChat", "Live Chat Exceotion: ", exception)
        exception.printStackTrace()
        val errorMess = exception.localizedMessage ?: ""
        val message = if (customMess.isNullOrEmpty()) errorMess else customMess
        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun SignInApp(email: String, pass: String) {
        inProcess.value = true
        firestore.collection(KEY_USER).whereEqualTo("userEmail", email)
            .get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    show_toast("Account is not registered in the app")
                    inProcess.value = false
                } else {
                    val userDocument = querySnapshot.documents[0]
                    if (userDocument.getString("password") == pass) {
                        auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                signIn.value = true
                                inProcess.value = false
                                auth.currentUser?.uid.let {
                                    getUserData(uuID = auth.currentUser?.uid)
                                }
                            }

                        inProcess.value = true
                    } else {
                        show_toast("Invalid password")
                        inProcess.value = false
                    }
                }
            }
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
                            Filter.equalTo("user1.email", email),
                            Filter.equalTo("user2.email", userData.value?.userEmail)
                        ),
                        Filter.and(
                            Filter.equalTo("user1.email", userData.value?.userEmail),
                            Filter.equalTo("user2.email", email)
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


}






















