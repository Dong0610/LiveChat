package dong.duan.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dong.duan.livechat.Screen.ListChatScreen
import dong.duan.livechat.Screen.ProfileScreen
import dong.duan.livechat.Screen.ScreenVModel
import dong.duan.livechat.Screen.SignInScreen
import dong.duan.livechat.Screen.SignUpScreen
import dong.duan.livechat.Screen.SingleChatScreen
import dong.duan.livechat.Screen.SingleStatusScreen
import dong.duan.livechat.Screen.StatusScreen
import dong.duan.livechat.ui.theme.LiveChatTheme

sealed class DestinationScreen(var route: String) {
    object SignUp : DestinationScreen("signup")
    object SignIn : DestinationScreen("signin")
    object Profile : DestinationScreen("profile")
    object ListChat : DestinationScreen("Listchat")
    object SingleChat : DestinationScreen("singlechat/{chatID}") {
        fun createRoute(chatID:String) = "singlechat/$chatID"
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
        val screenVModel : ScreenVModel by viewModels()

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
                val chatID= it.arguments?.getString("chatID")
                SingleChatScreen(navController, vm,chatID,screenVModel)
            }
            composable(DestinationScreen.Status.route) {
                StatusScreen(navController, vm)
            }
            composable(DestinationScreen.SingleStatus.route) {
                SingleStatusScreen(navController, vm)
            }
        }
    }



}
