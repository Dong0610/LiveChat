package dong.duan.livechat.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.ui.theme.WHITE

fun NavigateTo(navController: NavController, router: String) {
    navController.navigate(router) {
        popUpTo(router)
        launchSingleTop = true
    }
}

@Composable
fun CommonProgressBar() {
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(WHITE)
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommonDivider(modifier: Modifier=Modifier) {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun CheckSignIn(vm: LCViewModel, navController: NavController) {
    var alredySignIn = remember {
        mutableStateOf(false)

    }
    var SignIn = vm.signIn.value
    if (SignIn && !alredySignIn.value) {
        alredySignIn.value = true
        navController.navigate(DestinationScreen.ListChat.route)
        {
            popUpTo(0)
        }
    }

}