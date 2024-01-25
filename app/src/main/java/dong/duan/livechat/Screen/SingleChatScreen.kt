package dong.duan.livechat.Screen

import android.annotation.SuppressLint
import android.graphics.Point
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.Model.ChatData
import dong.duan.livechat.Model.MessIcon
import dong.duan.livechat.Model.Message
import dong.duan.livechat.Model.UserData
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.APP_COLOR
import dong.duan.livechat.ui.theme.LIGHT_GREY
import dong.duan.livechat.ui.theme.MEDIUM_BLUE
import dong.duan.livechat.ui.theme.PURPLE
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.widget.CommonDivider
import dong.duan.livechat.widget.CommonImage
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SingleChatScreen(
    navController: NavHostController,
    vm: LCViewModel, users: UserData?
) {
    val chatData = MutableLiveData<ChatData?>(null)
    if (users != null) {
        vm.checkChatExit(users) {
            chatData.value = it
            vm.initMessage(it?.chatID)
        }
    }
    var message by remember { mutableStateOf(TextFieldValue("")) }

    val listMessage = vm.listMessage.value

    var touchedPoint by remember { mutableStateOf(Offset.Zero) }
    val lazyColumnListState = rememberLazyListState()
    val corroutineScope = rememberCoroutineScope()
    LaunchedEffect(listMessage) {
        lazyColumnListState.animateScrollToItem(lazyColumnListState.layoutInfo.totalItemsCount)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(WHITE)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        modifier = Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .size(24.dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CommonImage(
                        data = users?.imgUrl?.takeIf { it.isNotEmpty() } ?: R.drawable.user_solid,
                        modifier = Modifier.size(46.dp), contentScale = ContentScale.Crop

                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${users?.userName}",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.fira_bold))
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .size(24.dp),
                        contentDescription = null, tint = LIGHT_GREY
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

            }
            CommonDivider()
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxSize(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (vm.isExitChat.value) {

                    LazyColumn(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom,
                        state = lazyColumnListState
                    ) {

                        this.itemsIndexed(items = listMessage) { index, item ->
                            val indexChat =
                                listMessage.indexOfLast { it.senderID == vm.userSignIn.value?.userID }
                            MessageRow(
                                message = item,
                                imgUrl = R.drawable.user_solid,
                                senderID = vm.userSignIn.value!!.userID!!,
                                if (index == indexChat) false else false,

                                { l, t ->
                                    vm.pointLocation.value = Point(l.roundToInt(), t.roundToInt())
                                    vm.isLongPress.value = true
                                })
                        }
                    }
                } else {
                    Column(Modifier.weight(1f)) {
                        NoMessage(users)
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
                            .weight(1f)
                            .height(52.dp)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .background(APP_COLOR, shape = CircleShape),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.round_image),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                    }
                                    .size(18.dp),
                                tint = WHITE
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .background(APP_COLOR, shape = CircleShape),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {

                                    }
                                    .size(18.dp),
                                tint = WHITE
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 42.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 36.dp),
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Row(
                                    Modifier
                                        .background(WHITE)
                                        .wrapContentHeight()
                                ) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    BasicTextField(
                                        value = message,
                                        onValueChange = { message = it },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                            .wrapContentHeight(),
                                        textStyle = TextStyle(
                                            fontSize = 14.sp
                                        )
                                    )

                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            PURPLE,
                                            MEDIUM_BLUE
                                        )
                                    ), shape = CircleShape
                                ),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {

                                        if (!vm.isExitChat.value) {
                                            vm.createChat(users, message.text) {
                                                chatData.value = it
                                            }
                                        } else {
                                            vm.sendMessage(chatData.value, message.text)
                                        }


                                        message = TextFieldValue("")
                                    }
                                    .size(18.dp),
                                tint = WHITE
                            )
                        }

                    }
                }
            }
        }

    }


    if (vm.isLongPress.value) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x25FFFFFF))
                .combinedClickable(true, onClick = {
                    vm.isLongPress.value = false
                }),
        ) {
            Box(
                modifier = Modifier
                    .offset(vm.pointLocation.value.x.dp, vm.pointLocation.value.y.dp)
                    .height(50.dp)
                    .fillMaxWidth(0.8f)
                    .background(Color.Gray)
            ) {
                Text(text = "L: ${vm.pointLocation.value.x.dp}-- T${vm.pointLocation.value.y.dp}")

            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageRow(
    message: Message,
    imgUrl: Any,
    senderID: String,
    isEndItem: Boolean = false,
    longClick: (Float, Float) -> Unit,
) {
    val isSender = senderID == message.senderID

    val density = LocalDensity.current

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (message.senderID != senderID) {
                CommonImage(
                    data = imgUrl, modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(true, onLongClick = {
//                        longClick(touchedPoint.x,touchedPoint.y)
                    }, onClick = {

                    }),
                contentAlignment = if (message.senderID != senderID) Alignment.CenterStart else Alignment.CenterEnd
            ) {

                Box() {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row {
                            Text(
                                text = message.message.toString(),
                                color = Color.Black,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .border(
                                        border = BorderStroke(1.dp, Color.LightGray),
                                        shape = if (message.senderID != senderID) RoundedCornerShape(
                                            12.dp, 12.dp, 12.dp, 0.dp
                                        ) else RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                            if (isSender) {
                                Spacer(modifier = Modifier.width(5.dp))
                                CommonImage(
                                    data = imgUrl, modifier = Modifier
                                        .size(14.dp)
                                        .align(
                                            Alignment.Bottom
                                        )
                                )
                            } else {
                                Spacer(modifier = Modifier.width(20.dp))
                            }
                        }
                        if (isEndItem) {
                            Text(
                                text = message.message.toString(),
                                color = Color(0xFFD3D3D3), fontSize = 12.sp
                            )
                        } else {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                    }
                    Column(
                        Modifier
                            .padding(top = 5.dp)
                            .padding(
                                start = if (!isSender) 5.dp else 0.dp,
                                end = if (isSender) 25.dp else 0.dp
                            )

                            .align(
                                if (isSender) Alignment.BottomEnd else Alignment.BottomStart
                            ), verticalArrangement = Arrangement.Bottom
                    ) {
                        if (message.chatIcon == MessIcon.None) {
                            Box(modifier = Modifier.size(14.dp))
                        } else {
                            Icon(
                                painter = painterResource(id = message.chatIcon.icon!!),
                                contentDescription = "",
                                tint = message.chatIcon.color!!,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(if (isEndItem) 12.dp else 6.dp))
                    }
                }
            }

        }


    }
}


@Composable
fun NoMessage(users: UserData?) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonImage(data = users?.imgUrl?.takeIf { it.isNotEmpty() } ?: R.drawable.user_solid,
            modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = users?.userName ?: "",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.fira_bold))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = users?.userEmail ?: "",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.fira_bold))
        )
    }
}

@HiltViewModel
class ScreenVModel @Inject constructor() : ViewModel() {
    val top = mutableStateOf(0f)
    val left = mutableStateOf(0f)
}