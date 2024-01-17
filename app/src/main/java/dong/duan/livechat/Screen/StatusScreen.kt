package dong.duan.livechat.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dong.duan.livechat.LCViewModel

@Composable
fun StatusScreen(navController: NavHostController, vm: LCViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ChatListContent(modifier = Modifier.weight(1f), vm = vm, navController = navController)

        BottomNavigation(
            select = BottomNavigtionItem.STATUSLIST,
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.06f)
        )
    }
}
