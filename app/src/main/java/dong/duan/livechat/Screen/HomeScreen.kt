package dong.duan.livechat.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.widget.BottomNavigation
import dong.duan.livechat.widget.BottomNavigtionItem

@Composable
fun HomeScreen(navController: NavHostController, vm: LCViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        // Content Column
        Column(
            Modifier
                .weight(1f)
                .background(Color.Cyan)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Hello, this is your content!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        BottomNavigation(
            select = BottomNavigtionItem.HOMES,
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.06f)
                .background(Color.White)
        )
    }

}
