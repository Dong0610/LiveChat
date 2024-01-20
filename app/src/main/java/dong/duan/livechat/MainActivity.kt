package dong.duan.livechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dong.duan.lib.library.show_toast
import dong.duan.livechat.Screen.HomeScreen
import dong.duan.livechat.Screen.ListChatScreen
import dong.duan.livechat.Screen.ProfileScreen
import dong.duan.livechat.Screen.ScreenVModel
import dong.duan.livechat.Screen.SignInScreen
import dong.duan.livechat.Screen.SignUpScreen
import dong.duan.livechat.Screen.SingleChatScreen
import dong.duan.livechat.Screen.SingleStatusScreen
import dong.duan.livechat.Screen.SplashScreen
import dong.duan.livechat.Screen.StatusScreen
import dong.duan.livechat.ui.theme.LiveChatTheme
import dong.duan.livechat.widget.animComposable
import javax.annotation.Nullable


sealed class DestinationScreen(var route: String) {
    object SignUp : DestinationScreen("signup")
    object Splash : DestinationScreen("splash")
    object SignIn : DestinationScreen("signin")
    object Profile : DestinationScreen("profile")
    object ListChat : DestinationScreen("Listchat")
    object SingleChat : DestinationScreen("singlechat/{chatID}") {
        fun createRoute(chatID: String) = "singlechat/$chatID"
    }

    object Friend : DestinationScreen("friend")
    object SingleStatus : DestinationScreen("singlestatus/{chatID}") {
        fun createRoute(usID: String) = "singlestatus/$usID"
    }
    object Home : DestinationScreen("home")

}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: LCViewModel by viewModels()
    private val screenVModel: ScreenVModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
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
        NavHost(navController = navController, startDestination = DestinationScreen.Splash.route) {
            animComposable(DestinationScreen.Splash.route) {
                SplashScreen(navController, vm)
            }
            animComposable(DestinationScreen.Home.route) {
                HomeScreen(navController, vm)
            }
            animComposable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, vm)
            }
            animComposable(DestinationScreen.SignIn.route) {
                SignInScreen(navController, vm)
            }
            animComposable(DestinationScreen.ListChat.route) {
                ListChatScreen(navController, vm)
            }
            animComposable(DestinationScreen.Profile.route) {
                ProfileScreen(navController, vm)
            }
            animComposable(DestinationScreen.SingleChat.route) {
                val chatID = it.arguments?.getString("chatID")
                SingleChatScreen(navController, vm, chatID, screenVModel)
            }
            animComposable(DestinationScreen.Friend.route) {
                StatusScreen(navController, vm)
            }
            animComposable(DestinationScreen.SingleStatus.route) {
                SingleStatusScreen(navController, vm)
            }
        }
    }


}

