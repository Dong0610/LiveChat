package dong.duan.livechat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.APP_COLOR
import dong.duan.livechat.ui.theme.TRANSPARENT
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.utility.NavigateTo

enum class BottomNavigtionItem(var icon: Int, var screen: DestinationScreen) {
    CHATLIST(R.drawable.message_solid, DestinationScreen.ListChat),
    STATUSLIST(R.drawable.magnifying, DestinationScreen.Status),
    PROFILE(R.drawable.user_solid, DestinationScreen.Profile)
}

@Composable
fun BottomNavigation(
    select: BottomNavigtionItem,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .alpha(0.3f)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(WHITE)

        ) {
            for (item in BottomNavigtionItem.entries) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 6.dp, bottom = 4.dp)
                        .weight(1f)
                        .clickable {
                            NavigateTo(navController, item.screen.route)
                        },
                    colorFilter = if (item == select) ColorFilter.tint(Color(0xff027FFF)) else ColorFilter.tint(
                        Color.LightGray
                    )
                )
            }
        }
    }
}
