@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package dong.duan.livechat.Screen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.utility.CommonProgressBar
import dong.duan.livechat.utility.NavigateTo

@Preview
@Composable
fun ProfileScreen(navController: NavHostController, vm: LCViewModel) {
    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ProfileContent(vm, Modifier.weight(1f), onBack = {
                    NavigateTo(navController, DestinationScreen.ListChat.route)
                })

                BottomNavigation(
                    select = BottomNavigtionItem.PROFILE,
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.06f)
                )
            }
        }
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfileContent(vm: LCViewModel, modifier: Modifier = Modifier, onBack: () -> Unit) {
    val userName = mutableStateOf(TextFieldValue(vm.userData.value?.userName!!))
    val password = mutableStateOf(TextFieldValue(vm.userData.value?.password!!))
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack() })
            Text(text = "Save", modifier = Modifier.clickable { })

        }

        ProfileImage(imgUrl = vm.userImg.value ?: "", vm = vm)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = userName.value,
                onValueChange = {
                    password.value = it
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
            )
        }

    }
}

@Composable
fun ProfileImage(imgUrl: String, vm: LCViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri.let {
            vm.uploadImageToFirebase(uri)
        }
    }

    Box(
        Modifier
            .wrapContentSize()
            .padding(top = 50.dp)
    ) {
        Column(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {

                CommonImage(data = imgUrl, modifier =Modifier.height(180.dp)
                    .width(180.dp)
                    .alpha(
                        1f
                    ))


        }

    }
}


@Composable
fun CommonImage(
    data: Any? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {

    val painter = rememberImagePainter(
        data = data,
        builder = {
            allowHardware(false)
        }
    )

    Card(
        shape = CircleShape, modifier = modifier
            .alpha(
                1f
            )
    ) {
        Image(
            painter = painter,
            contentDescription = "...",
            contentScale = contentScale,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        )
    }


}






















