package dong.duan.livechat.utility

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