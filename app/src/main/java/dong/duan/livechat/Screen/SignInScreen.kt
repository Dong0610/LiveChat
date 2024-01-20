@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package dong.duan.livechat.Screen

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.components.BuildConfig


import dong.duan.lib.library.screen_width
import dong.duan.lib.library.show_toast
import dong.duan.livechat.AppContext
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.MEDIUM_BLUE
import dong.duan.livechat.ui.theme.ORANGE_RED
import dong.duan.livechat.ui.theme.RED
import dong.duan.livechat.ui.theme.ROYAL_BLUE
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.widget.CommonProgressBar

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("710462798836-9ksu1catn78rtnj2qv3hmm58r0lgpp9s.apps.googleusercontent.com") // Can be obtained in Google Cloud
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}

@Composable
@Preview
fun SignInScreen(navController: NavHostController, vm: LCViewModel) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }


    val client = remember { GoogleSignInHelper.getGoogleSignInClient(AppContext.context) }
    val request = remember { GoogleSignInHelper.getGoogleSignInRequest() }
    val signInResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK && result.data != null) {
            val credential = client.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if(idToken != null) {
                show_toast("Success:$idToken")
            } else {
               show_toast("Sign failed")
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_bg_app),
            contentDescription = null, contentScale = ContentScale.Fit, modifier =
            Modifier
                .width(screen_width.dp)


        )
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
        ) {
            Text(
                text = "Live Chat",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp),
                color = WHITE,
                fontSize = 32.sp,
                fontFamily = FontFamily(
                    Font(R.font.fira_bold)
                ),
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .padding(8.dp)
                )
            }
            Text(
                text = "Welcome back",
                fontSize = 24.sp,
                color = Color(0xFF5B02FF),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(vertical = 12.dp)),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.fira_reg)),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = email,
                    label = { Text(text = "Email") },
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier.fillMaxWidth(0.92f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text(text = "Password") },
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier.fillMaxWidth(0.92f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = arrayOf(
                                Color(0xFF00FE90),
                                Color(0xFF5B02FF)
                            ).toList()
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        vm?.SignIn(email.text, password.text)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.fira_medium)),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Forgot password",
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.fira_reg)),
                    fontSize = 16.sp, modifier = Modifier.clickable {
                        vm.resetPassword(email.text)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.fira_reg)),
                    fontSize = 16.sp
                )
                Text(
                    text = " Sign In",
                    color = Color(0xFF5B02FF),
                    fontFamily = FontFamily(Font(R.font.fira_reg)),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navController!!.navigate(DestinationScreen.SignUp.route)
                        {
                            popUpTo(0)
                        }
                    }
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = arrayOf(
                                RED,
                                ORANGE_RED
                            ).toList()
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {

                        client.beginSignIn(request).addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                val intentSender = task.result.pendingIntent.intentSender
                                val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                                signInResultLauncher.launch(intentSenderRequest)
                            } else {
                              show_toast(task.exception?.message.toString())
                            }
                        }

                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    tint = WHITE,
                    modifier = Modifier.padding(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sign in with Google",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.fira_medium)),
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .height(52.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = arrayOf(
                                MEDIUM_BLUE, ROYAL_BLUE
                            ).toList()
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        vm?.SignIn(email.text, password.text)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = null,
                    tint = WHITE,
                    modifier = Modifier.padding(12.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sign in with Facebook",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.fira_medium)),
                    fontSize = 16.sp
                )
            }
        }
        if (vm!!.inProcess.value) {
            CommonProgressBar()
        }
    }
}

