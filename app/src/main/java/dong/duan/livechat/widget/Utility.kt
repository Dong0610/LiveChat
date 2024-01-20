package dong.duan.livechat.widget

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dong.duan.ecommerce.library.log
import dong.duan.lib.library.width_percent
import dong.duan.livechat.DestinationScreen
import dong.duan.livechat.LCViewModel
import dong.duan.livechat.Model.Message
import dong.duan.livechat.Screen.CommonImage
import dong.duan.livechat.ui.theme.LIGHT_GREY
import dong.duan.livechat.ui.theme.WHITE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun NavigateTo(navController: NavController, router: String) {
    navController.navigate(router) {
        popUpTo(router)
        launchSingleTop = true
    }
}
@Composable
fun CommonProgressBar(speed:Int=1000) {
    val indicatorSize = width_percent(6.5f).dp
    val trackWidth: Dp = (indicatorSize * 0.1f)
    @Composable
    fun animateRotation(): Float {
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(speed, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
        return rotation
    }
    val commonModifier = Modifier
        .size(indicatorSize)
        .rotate(animateRotation())
    Row(
        modifier = Modifier

            .background(Color(0x10000000))
            .clickable(enabled = false) {}
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GradientProgressIndicator(
            progress = 100f,
            modifier = commonModifier,
            strokeWidth = trackWidth,
            gradientStart = ProgressBarColor.Blue.gradientStart,
            gradientEnd = ProgressBarColor.Purple.gradientEnd,
            trackColor = Color.LightGray,
        )
    }
}


enum class ProgressBarColor(val gradientStart: Color, val gradientEnd: Color) {
    Red(Color(254, 222, 224), Color(255, 31, 43)),
    Green(Color(168, 242, 205), Color(38, 222, 129)),
    Blue(Color(219, 229, 251), Color(75, 123, 236)),
    Purple(Color(224, 204, 255), Color(98, 0, 254, 255));

    operator fun invoke(): Color {
        return gradientEnd
    }
}

val trackColor = WHITE


@Composable
fun CommonDivider(modifier: Modifier = Modifier) {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = modifier
            .alpha(0.3f)
    )
}

@Composable
fun ListChatItem(imgUrl: String? = "", name: String? = "", onItemClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { onItemClick.invoke() }) {
        CommonImage(
            data = imgUrl,
            Modifier
                .width(75.dp)
                .height(75.dp)
                .padding(3.dp), contentScale = ContentScale.Crop
        )
        Text(text = name ?: "Error")
    }
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomTextField() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        Modifier
            .height(48.dp)
            .padding(10.dp)
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
                    value = name,
                    keyboardOptions = KeyboardOptions.Default,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    )
                )
                Box(
                    Modifier
                        .padding(vertical = 2.dp)
                        .width(1.dp)
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}
//
//@SuppressLint("UnrememberedMutableState")
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun MessageRow(
//    message: Message,
//    imgUrl: String,
//    senderID: String,
//    longClick: (Float, Float) -> Unit
//) {
//    var left by remember { mutableStateOf(0f) }
//    var top by remember { mutableStateOf(0f) }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .onGloballyPositioned {
//                left = it.positionInParent().x
//                top = it.positionInParent().y
//                log("TEST","${it.positionInWindow().x} --- ${it.positionInWindow().y} ")
//            },
//        horizontalArrangement = if (message.senderID == senderID) Arrangement.End else Arrangement.Start
//    ) {
//
//        if (message.senderID != senderID) {
//            CommonImage(
//                data = imgUrl, modifier = Modifier
//                    .size(32.dp)
//                    .align(Alignment.CenterVertically)
//            )
//        }
//
//        Spacer(modifier = Modifier.width(8.dp))
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(0.85f)
//                .combinedClickable(true, onLongClick = {
//                    longClick(left, top)
//                }, onClick = {
//
//                })
//        ) {
//            Box(
//                modifier = Modifier
//                    .border(
//                        border = BorderStroke(1.dp, Color.LightGray),
//                        shape = RoundedCornerShape(6.dp)
//                    )
//                    .padding(8.dp)
//                    .wrapContentSize()
//                    .align(if (message.senderID == senderID) Alignment.CenterEnd else Alignment.CenterStart)
//            ) {
//                Text(
//                    text = message.message.toString(),
//                    color = Color.Black,
//                    modifier = Modifier
//                        .wrapContentSize()
//                )
//            }
//        }
//    }
//}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageRow(
    message: Message,
    imgUrl: String,
    senderID: String,
    longClick: (Float, Float) -> Unit
) {
    var viewLocation by remember { mutableStateOf(IntOffset(0, 0)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                viewLocation = IntOffset(
                    x = coordinates.positionInRoot().x.roundToInt(),
                    y = coordinates.positionInRoot().y.roundToInt()
                )
            }
    ) {

        if (message.senderID != senderID) {
            CommonImage(
                data = imgUrl, modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .combinedClickable(true, onLongClick = {
                    longClick(viewLocation.x.toFloat(), viewLocation.y.toFloat())
                    log("TEST", "x:${viewLocation.x} -- y:${viewLocation.y}")
                }, onClick = {

                })
        ) {
            Box(
                modifier = Modifier
                    .border(
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(8.dp)
                    .wrapContentSize()
                    .align(if (message.senderID == senderID) Alignment.CenterEnd else Alignment.CenterStart)
            ) {
                Text(
                    text = message.message.toString(),
                    color = Color.Black,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }
    }
}



@Composable
fun DotsFlashing(dotSize: Dp =18.dp,color: Color= Color.Blue,delayUnit:Int=750) {
    val minAlpha = 0.1f

    @Composable
    fun Dot(
        alpha: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .alpha(alpha)
            .background(
                color = color,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                minAlpha at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                minAlpha at delay + delayUnit * 2
            }
        ), label = ""
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(delayUnit)
    val alpha3 by animateAlphaWithDelay(delayUnit * 2)
    val alpha4 by animateAlphaWithDelay(delayUnit * 3)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 12.dp

        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha4)
    }
}








