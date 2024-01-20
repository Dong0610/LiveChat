package dong.duan.livechat.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.duan.lib.library.show_toast
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.Model.ChatData
import dong.duan.livechat.ui.theme.APP_COLOR
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.widget.CommonDivider
import dong.duan.livechat.widget.MessageRow
import javax.inject.Inject

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleChatScreen(
    navController: NavHostController,
    vm: LCViewModel,
    chatID: String?,
    screenVModel: ScreenVModel
) {
    var chatData: ChatData? = null
    vm.currentChat(chatID!!) {
        chatData = it
    }
    vm.InitMessage(chatID)
    val listMessage = vm.listMessage.value

    var message by remember { mutableStateOf(TextFieldValue("")) }
    val left = mutableStateOf(0f)
    val top = mutableStateOf(0f)
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    modifier = Modifier.clickable { navController.popBackStack() },
                    contentDescription = null
                )
                CommonImage(
                    data = chatData!!.reciveUser.imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .padding(start = 0.dp)
                        .padding(start = 10.dp)
                )
                Text(text = "Chat ID: ${chatData?.reciveUser?.name}")
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxSize(1f)
            ) {
                LazyColumn(Modifier.weight(1f)) {
                    items(listMessage) {
                        MessageRow(
                            message = it,
                            imgUrl = chatData!!.reciveUser.imageUrl!!,
                            senderID = vm.userData.value!!.userID!!,
                            { l, t ->
                                show_toast("${l}--${t}")
                                screenVModel.left.value = l
                                screenVModel.top.value = t
                                vm.isLongPress.value = true
                            })

                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                ) {
                    CommonDivider(modifier = Modifier.padding(0.dp))
                    Row(
                        Modifier
                            .height(52.dp)
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .wrapContentHeight(),
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(WHITE)
                                    .wrapContentSize()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = message,
                                    keyboardOptions = KeyboardOptions.Default,
                                    onValueChange = { message = it },
                                    modifier = Modifier
                                        .weight(1f),
                                    textStyle = TextStyle(
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .background(APP_COLOR, shape = CircleShape)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        vm.SendMessage(
                                            message.text,
                                            chatData
                                        )
                                        message = TextFieldValue("")
                                    },
                                tint = WHITE
                            )
                        }

                    }
                }
            }
        }

    }

    if (vm.isLongPress.value) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xcfffffff))
                .combinedClickable(true, onClick = {
                    vm.isLongPress.value = false
                }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = " L ${left.value.toInt()} T ${top.value.toInt()}")
        }
    }
}

@HiltViewModel
class ScreenVModel @Inject constructor() : ViewModel() {
    val top = mutableStateOf(0f)
    val left = mutableStateOf(0f)
}