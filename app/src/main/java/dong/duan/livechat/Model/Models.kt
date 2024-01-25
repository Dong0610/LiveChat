package dong.duan.livechat.Model

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.ORANGE_RED
import java.io.Serializable
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
) : Serializable {
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
    var receiveID: String = "", var chatType: ChatType = ChatType.MESSAGE,
    var sendDel: Boolean = false, var reciveDel: Boolean = false,
    var chatIcon:MessIcon=MessIcon.None
) : Serializable

enum class ChatType {
    MESSAGE,
    IMAGE,
    LOCATION
}

enum class MessIcon(var icon:Int?,val color: Color?) {
    None(icon=null,null),
    Love(icon = R.drawable.face_heart, Color.Red),
    Like(R.drawable.round_thumb_up, Color.Blue),
    Smile(R.drawable.face_laugh, Color.Yellow),
    Angry(R.drawable.face_angry, ORANGE_RED)
}

data class ChatUser(
    var userID: String? = "",
    var name: String? = "",
    var email: String? = "",
    var imageUrl: String? = "", var isBlock: Boolean = false
)

data class ChatData(
    var chatID: String? = "",
    var chatTimeCreate: String = Date().time.toString(),
    var sendUser: ChatUser = ChatUser(),
    var reciveUser: ChatUser = ChatUser(),
    var chatLastMess: String = "",
    var chatMessTime: String = "",

    )