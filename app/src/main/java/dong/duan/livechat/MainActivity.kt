package dong.duan.livechat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dong.duan.livechat.Screen.ListChatScreen
import dong.duan.livechat.Screen.ProfileScreen
import dong.duan.livechat.Screen.SignInScreen
import dong.duan.livechat.Screen.SignUpScreen
import dong.duan.livechat.Screen.SingleChatScreen
import dong.duan.livechat.Screen.SingleStatusScreen
import dong.duan.livechat.Screen.StatusScreen
import dong.duan.livechat.ui.theme.LiveChatTheme
import dong.duan.livechat.ui.theme.WHITE

sealed class DestinationScreen(var route: String) {
    object SignUp : DestinationScreen("signup")
    object SignIn : DestinationScreen("signin")
    object Profile : DestinationScreen("profile")
    object ListChat : DestinationScreen("Listchat")
    object SingleChat : DestinationScreen("singlechat/{chatID}") {
        fun createRoute(id: String) = "singlechat/$id"
    }

    object Status : DestinationScreen("status")
    object SingleStatus : DestinationScreen("singlestatus/{chatID}") {
        fun createRoute(usID: String) = "singlestatus/$usID"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme() {

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ChatNavigation()
                }
            }
        }

    }

    @Preview
    @Composable
    fun ChatNavigation() {
        val navController = rememberNavController()
        val vm: LCViewModel by viewModels()
        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route) {

            composable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreen.SignIn.route) {
                SignInScreen(navController, vm)
            }
            composable(DestinationScreen.ListChat.route) {
                ListChatScreen(navController, vm)
            }
            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController, vm)
            }
            composable(DestinationScreen.SingleChat.route) {
                SingleChatScreen(navController, vm)
            }
            composable(DestinationScreen.Status.route) {
                StatusScreen(navController, vm)
            }
            composable(DestinationScreen.SingleStatus.route) {
                SingleStatusScreen(navController, vm)
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun CustomTextField() {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        Row(
            Modifier
                .height(48.dp)
                .padding(10.dp)
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
                        value = name,
                        keyboardOptions = KeyboardOptions.Default,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        textStyle = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                    Box(
                        Modifier
                            .padding(vertical = 2.dp)
                            .width(1.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            }
        }
    }

}
