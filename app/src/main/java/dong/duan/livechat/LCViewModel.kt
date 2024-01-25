package dong.duan.livechat

import android.annotation.SuppressLint
import android.graphics.Point
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.duan.lib.library.show_toast
import dong.duan.livechat.Model.ChatData
import dong.duan.livechat.Model.ChatType
import dong.duan.livechat.Model.ChatUser
import dong.duan.livechat.Model.MessIcon
import dong.duan.livechat.Model.Message
import dong.duan.livechat.Model.UserData
import dong.duan.livechat.utility.KEY_CHAT
import dong.duan.livechat.utility.KEY_USER
import dong.duan.livechat.utility.toJson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class LCViewModel @Inject constructor() : ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
    val database: FirebaseDatabase = Firebase.database
    val storage: FirebaseStorage = Firebase.storage


    val userSignIn = mutableStateOf<UserData?>(null)
    val isSignApp = mutableStateOf(false)

    val inProcess = mutableStateOf(false)

    val listChatData = mutableStateOf(mutableListOf<ChatData>())

    val listMessage = mutableStateOf<MutableList<Message>>(mutableListOf())

    init {
        if (auth.currentUser != null) {
            getUserSignIn(auth.currentUser!!.uid)

        }
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { fetchUsers() }
        }
    }

    fun initMessage(chatID: String?) {
        if (!chatID.isNullOrEmpty()) {
            database.getReference(KEY_CHAT)
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
                        show_toast(error.message)
                    }
                })

        }

    }

    fun fetchChatData() {
        firestore.collection(KEY_CHAT)
            .where(
                Filter.or(
                    Filter.equalTo("sendUser.email", userSignIn.value?.userEmail),
                    Filter.equalTo("reciveUser.email", userSignIn.value?.userEmail)
                )
            ).addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    showToast(exception)
                    return@addSnapshotListener
                }
                val listChat = mutableListOf<ChatData>()
                for (document in querySnapshot!!) {
                    val chatDtList = document.toObject(ChatData::class.java)
                    listChat.add(chatDtList)
                }
                listChat.sortByDescending { it.chatMessTime }
                listChatData.value = listChat
            }

    }


    val listUser = mutableStateOf<MutableList<UserData>>(mutableListOf())
    suspend fun fetchUsers() {
        val listFetch = mutableListOf<UserData>()

        firestore.collection(KEY_USER)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val userData = document.toObject(UserData::class.java)
                    userData?.let {
                        if (it.userID != userSignIn.value?.userID) {
                            listFetch.add(it)
                        }

                    }
                }
                listUser.value = listFetch
            }
            .addOnFailureListener { exception ->
                showToast(exception)
            }
    }


    private fun getUserSignIn(uid: String) {
        firestore.collection(KEY_USER)
            .document(uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    val userData = task.result.toObject(UserData::class.java)
                    inProcess.value = false
                    isSignApp.value = true
                    userSignIn.value = userData
                } else {
                    showToast(task.exception!!)
                }
            }
            .addOnFailureListener {
                showToast(it)
            }
    }

    fun signUp(name: String, email: String, pass: String) {
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            show_toast("Pleases enter all value!")
        } else {
            inProcess.value = true
            firestore.collection(KEY_USER)
                .whereEqualTo("userEmail", email)
                .get()
                .addOnSuccessListener { task ->
                    if (!task.isEmpty) {
                        inProcess.value = false
                        show_toast("This email is used in app")

                    } else {
                        auth.createUserWithEmailAndPassword(email, pass)
                            .addOnSuccessListener {
                                createUser(it.user!!.uid, name, email, pass);
                            }
                            .addOnFailureListener {
                                show_toast(it.message.toString())
                                inProcess.value = false
                            }
                    }

                }
                .addOnFailureListener {
                    show_toast(it.message.toString())
                    inProcess.value = false
                }
        }

    }

    private fun createUser(uid: String, name: String, email: String, pass: String) {
        uid.let {
            val userData = UserData().apply {
                userID = it
                userName = name
                userEmail = email
                password = pass
                phoneNum = ""
                regTime = Date().time.toString()
                imgUrl = ""
                isAvailable = true
            }
            firestore.collection(KEY_USER)
                .document(userData.userID!!)
                .set(userData)
                .addOnCompleteListener {
                    userSignIn.value = userData
                    auth.signInWithEmailAndPassword(email, pass)
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            show_toast("The confirmation letter has been sent to the registered email!")
                        }
                    isSignApp.value = true
                    inProcess.value = false
                }
                .addOnFailureListener {
                    showToast(it)
                    inProcess.value = false
                }
        }
    }

    fun showToast(e: Exception) {
        show_toast("Exception: " + e.message.toString())
    }

    fun uploadImageToFirebase(uri: Uri?) {

    }

    fun signIn(emal: String, pass: String) {
        if (emal.isEmpty() or pass.isEmpty()) {
            show_toast("Pleases enter value to sign in app")
        } else {
            inProcess.value = true
            firestore.collection(KEY_USER)
                .whereEqualTo("userEmail", emal)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isEmpty) {
                        auth.signInWithEmailAndPassword(emal, pass)
                            .addOnCompleteListener {
                                getUserSignIn(it.result.user!!.uid)
                            }
                            .addOnFailureListener {
                                showToast(it)
                                inProcess.value = false
                            }
                    } else {
                        show_toast("Email not registered in app")
                        inProcess.value = false
                    }
                }
                .addOnFailureListener {
                    showToast(it)
                    inProcess.value = false
                }
        }
    }

    val isExitChat = mutableStateOf(false)

    fun checkChatExit(users: UserData?, chatData: (ChatData?) -> Unit) {
        firestore.collection(KEY_CHAT)
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("reciveUser.email", users!!.userEmail),
                        Filter.equalTo("sendUser.email", userSignIn.value?.userEmail)
                    ),
                    Filter.and(
                        Filter.equalTo("reciveUser.email", userSignIn.value?.userEmail),
                        Filter.equalTo("sendUser.email", users.userEmail)
                    )
                )
            )
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chat = task.result?.toObjects(ChatData::class.java)
                    if (chat != null && chat.isNotEmpty()) {
                        chatData(chat[0])
                        isExitChat.value = true
                    } else {
                        isExitChat.value = false
                        chatData(null)
                    }
                } else {
                    show_toast("Task failed with exception: ${task.exception}")
                }
            }
            .addOnFailureListener {
                showToast(it)
            }
    }

    fun createChat(users: UserData?, text: String, calback: (ChatData) -> Unit) {
        val newID = firestore.collection(KEY_CHAT).document().id;
        val sendUs = userSignIn.value!!
        val chatData = ChatData().apply {
            chatID = newID
            chatTimeCreate = Date().time.toString()
            sendUser = ChatUser().apply {
                userID = sendUs.userID
                name = sendUs.userName
                email = sendUs.userEmail
                imageUrl = sendUs.imgUrl
                isBlock = false
            }
            reciveUser = ChatUser().apply {
                userID = users!!.userID
                name = users.userName
                email = users.userEmail
                imageUrl = users.imgUrl
                isBlock = false
            }
            chatLastMess = text
            chatLastMess = Date().time.toString()
        }
        firestore.collection(KEY_CHAT)
            .document(newID)
            .set(chatData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    calback(chatData)
                    sendMessage(chatData, text)
                } else {
                    showToast(it.exception!!)
                }
            }.addOnFailureListener {
                showToast(it)
            }
    }



    fun sendMessage(chatData: ChatData?, text: String) {
        val newKey: String = database.getReference().child(KEY_CHAT).push().key ?: ""

        val mess = Message().apply {
            id = newKey
            message = text.trim()
            time = System.currentTimeMillis().toString()
            senderID = userSignIn.value!!.userID.toString()
            if (chatData != null) {
                receiveID =
                    userSignIn.value!!.userID.toString().takeIf { it == chatData.sendUser.userID }
                        ?: chatData.reciveUser.userID.toString()
            }
            chatType = ChatType.MESSAGE
            sendDel = false
            reciveDel = false
            chatIcon= MessIcon.Love
        }

        database.getReference(KEY_CHAT)
            .child(chatData!!.chatID!!)
            .push()
            .setValue(mess)
            .addOnSuccessListener {
                updateLastMess(chatData, mess)
                Log.d("SendMess", "sendMessage: Success")
            }.addOnFailureListener {
                showToast(it)
            }
    }

    private fun updateLastMess(chatData: ChatData, mess: Message) {
        firestore.collection(KEY_CHAT)
            .document(chatData.chatID!!)
            .update(mapOf("chatLastMess" to mess.message, "chatMessTime" to mess.time))
            .addOnSuccessListener { Log.d("SendMess", "updateLastMess: Success:${mess.toJson()}") }
            .addOnFailureListener { Log.d("SendMess", "updateLastMess: ${it.message}") }
    }

    val pointLocation = mutableStateOf<Point>(Point(0,0))




    val isLongPress= mutableStateOf(false)



}






















