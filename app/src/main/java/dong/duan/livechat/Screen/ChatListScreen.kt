package dong.duan.livechat.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.APP_COLOR
import dong.duan.livechat.ui.theme.LIGHT_CORAL
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.utility.ChatData
import dong.duan.livechat.utility.CommonProgressBar

@Composable
fun ListChatScreen(navController: NavHostController, vm: LCViewModel) {
    if (vm.inProcessChat.value) {
        CommonProgressBar()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChatListContent(modifier = Modifier.weight(1f), vm)

            BottomNavigation(
                select = BottomNavigtionItem.CHATLIST,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.06f)
            )
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListContent(modifier: Modifier = Modifier, vm: LCViewModel) {
    val chats = vm.chats.value

    val userData = vm.userData.value
    val showDialog = remember {
        mutableStateOf(false)
    }
    val onFabClick: () -> Unit = { showDialog.value = true }
    val onDismis: () -> Unit = { showDialog.value = false }
    val onAddChat: (String) -> Unit = {
        vm.onAddChat(it)
        showDialog.value = false
    }
    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {

        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = { onFabClick() }, // Ensure you call the function here
                    onDismis = { onDismis() }, // Ensure you call the function here
                    onAddChat = { onAddChat(it) } // Ensure you call the function here
                )
            }, modifier = Modifier.padding(6.dp),
            content = {
                Column() {
                    TitleText(text = "Chats")
                    Column(
                        modifier = Modifier
                            .weight(1f, true)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        if (chats.size == 0) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "No chat available",
                                )
                            }
                        } else {
                            ListChats(chats)
                        }

                    }

                }
            }
        )

    }
}

@Composable
fun ListChats(chats: MutableList<ChatData>) {

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

@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismis: () -> Unit,
    onAddChat: (String) -> Unit
) {
    var addChatMember = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismis.invoke()
                addChatMember.value = ""
            }, confirmButton = {
                Button(onClick = { onAddChat(addChatMember.value) }) {
                    Text(text = "Add Chat")
                }
            }, title = { Text(text = "Add Chat") }, text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = addChatMember.value,

                        label = { Text(text = "Email") },
                        onValueChange = {
                            addChatMember.value = it
                        },
                        modifier = Modifier.fillMaxWidth(0.92f),
                    )
                }
            })
    }
    FloatingActionButton(
        onClick = { onFabClick() },
        containerColor = APP_COLOR,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 20.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add", tint = WHITE)
    }
}














