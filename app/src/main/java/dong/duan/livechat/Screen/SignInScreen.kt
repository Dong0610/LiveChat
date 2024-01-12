@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package dong.duan.livechat.Screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.utility.CheckSignIn
import dong.duan.livechat.utility.CommonProgressBar


@Composable
@Preview
fun SignInScreen(navController: NavHostController?, vm: LCViewModel?) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    CheckSignIn(vm = vm!!, navController = navController!!)
    Box(
        Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_bg_app),
            contentDescription = null, contentScale = ContentScale.Crop, modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f)

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
                    painter = painterResource(id = R.drawable.chat_icon),
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
                color = Color(0xFF673AB7),
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
                                Color(0xFF0076FE),
                                Color(0xFF027FFF)
                            ).toList()
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        vm?.SignInApp(email.text, password.text)
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
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),

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
                    text = " Sign Up",
                    color = Color(0xFF673AB7),
                    fontFamily = FontFamily(Font(R.font.fira_reg)),
                    fontSize = 16.sp,
                    modifier=Modifier.clickable {
                        navController!!.navigate(DestinationScreen.SignUp.route)
                        {
                            popUpTo(0)
                        }
                    }
                )
            }
        }

        if(vm.inProcess.value){
            CommonProgressBar()
        }
    }
}