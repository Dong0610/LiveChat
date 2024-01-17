package dong.duan.livechat.utility

import java.util.Date

data class UserData(
    var userID: String? = "",
    var userName: String? = "",
    var userEmail: String? = "",
    var password: String? = "",
    var imgUrl: String? = ""
) {
    fun toMap() =
        mapOf(
            "UserID" to userID,
            "UserName" to userName,
            "UserEmail" to userEmail,
            "UserPass" to password,
            "UserImg" to imgUrl

        )

}

data class Message(var id:String?="",var message:String?="",var time:String=Date().time.toString(),var senderID:String="",var receiveID:String="")

data class ChatUser(
    var userID: String? = "",
    var name: String? = "",
    var email: String? = "",
    var imageUrl: String? = ""
)

data class ChatData(
    var chatID: String? = "",
    var sendUser: ChatUser = ChatUser(),
    var reciveUser: ChatUser = ChatUser()
)