package dong.duan.livechat.Screen

import dong.duan.livechat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.utility.DotsFlashing
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, vm: LCViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_app),
                    contentDescription = "Logo App"
                )
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.8f))

            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f), horizontalArrangement = Arrangement.Center
            ) {
                DotsFlashing(dotSize = 12.dp)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        delay(1500)

        navController.run { navigate(DestinationScreen.SignIn.route) }
    }
}
