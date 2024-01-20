package dong.duan.livechat.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "User")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "UserID")
    var userID: String? = "",

    @ColumnInfo(name = "UserName")
    var userName: String? = "",

    @ColumnInfo(name = "UserEmail")
    var userEmail: String? = "",

    @ColumnInfo(name = "PhoneNum")
    var phoneNum: String? = "",

    @ColumnInfo(name = "UserPass")
    var password: String? = "",

    @ColumnInfo(name = "UserImg")
    var imgUrl: String? = "",

    @ColumnInfo(name = "UserToken")
    var userToken: String? = "",

    @ColumnInfo(name = "RegTime")
    var regTime: String? = "",

    @ColumnInfo(name = "IsAvailable")
    var isAvailable: Boolean? = true
) {
    fun toMap(): Map<String, String?> =
        mapOf(
            "UserID" to userID,
            "UserName" to userName,
            "UserEmail" to userEmail,
            "PhoneNum" to phoneNum,
            "UserPass" to password,
            "UserImg" to imgUrl,
            "UserToken" to userToken,
            "RegTime" to regTime
        )
}


data class Message(
    var id: String? = "",
    var message: String? = "",
    var time: String = Date().time.toString(),
    var senderID: String = "",
    var receiveID: String = ""
)

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