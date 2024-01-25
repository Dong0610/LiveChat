package dong.duan.livechat.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.Model.ChatData
import dong.duan.livechat.Model.ChatUser
import dong.duan.livechat.Model.UserData
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.utility.toJson
import dong.duan.livechat.widget.BottomNavigation
import dong.duan.livechat.widget.BottomNavigtionItem
import dong.duan.livechat.widget.CommonDivider
import dong.duan.livechat.widget.CommonImage

@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListChatScreen(navController: NavHostController, vm: LCViewModel) {
    var searchKey by remember { mutableStateOf(TextFieldValue("")) }
    var isDialog = mutableStateOf(false)
    vm.fetchChatData()
//    if (vm.inProcessChat.value) {
//        CommonProgressBar()
//    } else {
    Scaffold(
        floatingActionButton = {
            SearchDialog(navController, vm,
                showDialog = isDialog.value,
                onDismis = { isDialog.value = false },
                onAddChat = {}
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    Row(
                        Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .padding(0.dp)
                            .padding(horizontal = 8.dp)
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            Modifier.padding(start = 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CommonImage(
                                modifier = Modifier.size(48.dp),
                                data = vm.userSignIn.value?.imgUrl ?: R.drawable.user_solid
                            )
                            Text(
                                text = vm.userSignIn.value?.userName.toString(),
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.fira_bold)),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        Box(
                            Modifier
                                .size(32.dp)
                                .background(Color(0xFFCECECC), shape = CircleShape)
                                .clickable {
                                    isDialog.value = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add",
                                tint = Color(0xFF333333)
                            )
                        }
                    }
                    CommonDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        Modifier
                            .height(48.dp)
                            .padding(horizontal = 12.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(Modifier.background(WHITE)) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(24.dp)
                                )
                                BasicTextField(

                                    value = searchKey,
                                    onValueChange = { searchKey = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp),
                                    textStyle = TextStyle(
                                        fontSize = 18.sp
                                    )
                                )

                            }
                        }
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        vm.listChatData.value.forEach {
                            RowItemChat(it, vm.userSignIn.value!!) {
                                val userData= UserData().apply {
                                    userID= it.userID
                                    userName= it.name
                                    userEmail= it.email
                                    imgUrl=it.imageUrl
                                }
                                navController.navigate(
                                    route = DestinationScreen.SingleChat.route,
                                    builder = {
                                        with(navController.currentBackStackEntry?.savedStateHandle) {
                                            this?.set("us", userData.toJson())
                                        }
                                    }
                                )
                            }
                        }

                    }

                }

                BottomNavigation(
                    select = BottomNavigtionItem.CHATLIST,
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.06f)
                )
            }
        })


}

@Composable
fun RowItemChat(chatData: ChatData, userData: UserData, onSelect: (ChatUser) -> Unit) {
    val receiveUS = if (chatData.sendUser.userID == userData.userID) chatData.reciveUser else chatData.sendUser
    val lastMess = if (chatData.sendUser.userID == userData.userID) "You: ${chatData.chatLastMess}" else  "${receiveUS.name}: ${chatData.chatLastMess}"
    Row(
        Modifier
            .padding(6.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clickable { onSelect(receiveUS) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        CommonImage(modifier = Modifier.size(54.dp), data = receiveUS.imageUrl.toString().takeIf { it.isNotEmpty() }?:R.drawable.user_solid)
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Text(
                text = receiveUS.name.toString(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.fira_bold)),
                fontWeight = FontWeight(600)
            )
            Text(text = lastMess, fontSize = 14.sp)
        }
        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun TitleText(text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.fira_bold)),
            fontSize = 18.sp
        )
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchDialog(
    navController: NavHostController,
    vm: LCViewModel,
    showDialog: Boolean,
    onDismis: () -> Unit,
    onAddChat: (String) -> Unit
) {
    var addChatMember = remember {
        mutableStateOf("")
    }
    var searchKey by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val listUserData = remember {
        vm.listUser.value
    }

    val filteredListUser by derivedStateOf {
        listUserData.filter {
            it.userName?.contains(searchKey.text, ignoreCase = true) == true ||
                    it.userEmail?.contains(searchKey.text, ignoreCase = true) == true
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismis() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WHITE)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_app),
                                contentDescription = "imageDescription",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(24.dp)
                            )
                            Text(
                                text = "Add new chat",
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                        Box(
                            Modifier
                                .size(24.dp)
                                .background(Color(0xFFDDEEEE), shape = CircleShape)
                                .clickable {
                                    onDismis()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF9B9B9B)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        Modifier
                            .height(42.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(Modifier.background(WHITE)) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(24.dp)
                                )
                                BasicTextField(

                                    value = searchKey,
                                    onValueChange = { searchKey = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp),
                                    textStyle = TextStyle(
                                        fontSize = 14.sp
                                    )
                                )

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        val userData = UserData().apply {
                            userName = "Dong"
                            userEmail = "123@gmail.com"
                            imgUrl = ""
                        }

                        filteredListUser.forEach {
                            ItemRowUser(userData = it, select = {
                                navController.navigate(
                                    route = DestinationScreen.SingleChat.route,
                                    builder = {
                                        with(navController.currentBackStackEntry?.savedStateHandle) {
                                            this?.set("us", it.toJson())
                                        }
                                    }
                                )
                            })
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ItemRowUser(userData: UserData, select: (UserData) -> Unit) {
    Row(
        Modifier
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .height(46.dp)
            .fillMaxWidth()
            .clickable { select(userData) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        CommonImage(
            modifier = Modifier.size(32.dp), data = userData.imgUrl.toString().takeIf { it.isNotEmpty() } ?:R.drawable.user_solid
        )

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Text(
                text = userData.userName.toString(),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.fira_reg)),
                fontWeight = FontWeight(600)
            )
            Text(
                text = userData.userEmail.toString(),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.fira_reg)),
                fontWeight = FontWeight(400)
            )
        }
    }
}











