package dong.duan.livechat.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.R
import dong.duan.livechat.ui.theme.LIGHT_GREY
import dong.duan.livechat.ui.theme.WHITE
import dong.duan.livechat.widget.BottomNavigation
import dong.duan.livechat.widget.BottomNavigtionItem
import dong.duan.livechat.widget.CommonDivider
import dong.duan.livechat.widget.CommonImage

@Composable
fun FriendScreen(navController: NavHostController, vm: LCViewModel) {
    var searchKey by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .weight(1f)

                .verticalScroll(rememberScrollState())
        ) {

            Row(
                Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .padding(0.dp)
                    .padding(horizontal = 12.dp)
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.padding(start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CommonImage(modifier = Modifier.size(50.dp), data = vm.userSignIn.value?.imgUrl?:"")
                    Text(
                        text = "Find new friend",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.fira_bold)),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                Box(
                    Modifier
                        .size(36.dp)
                        .background(LIGHT_GREY, shape = CircleShape)
                        .clickable {

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Add")
                }
            }
            CommonDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier
                    .height(48.dp)
                    .padding(horizontal = 12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(Modifier.background(WHITE)) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                        )
                        BasicTextField(

                            value = searchKey,
                            onValueChange = { searchKey = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 18.sp
                            )
                        )

                    }
                }
            }
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                for (i in 0..20) {
                  //  RowItemChat(it)
                }

            }

        }

        BottomNavigation(
            select = BottomNavigtionItem.FRIEND,
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.06f)
        )
    }
}
