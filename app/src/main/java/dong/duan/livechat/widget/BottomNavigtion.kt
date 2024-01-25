package dong.duan.livechat.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dong.duan.livechat.ui.theme.WHITE

enum class BottomNavigtionItem(var icon: Int, var screen: DestinationScreen) {
    CHATLIST(R.drawable.ic_messenger, DestinationScreen.ListChat),
    HOMES(R.drawable.ic_homes,DestinationScreen.Home),
    FRIEND(R.drawable.ic_users, DestinationScreen.Friend),
    PROFILE(R.drawable.user_solid, DestinationScreen.Profile)
}

@Composable
fun BottomNavigation(
    select: BottomNavigtionItem,
    navController: NavController?,
    modifier: Modifier = Modifier
) {
    val gradientColors = listOf(Color(0xFF02FF9A), Color(0xFF0622BD))

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
                            NavigateTo(navController!!, item.screen.route)
                        },
                    colorFilter = if (item == select) ColorFilter.lighting(Color(0xFF02FF9A), Color(0xFF0622BD)) else ColorFilter.tint(
                        Color.LightGray
                    )
                )
            }
        }
    }
}