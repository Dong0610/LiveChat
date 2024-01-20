package dong.duan.livechat.widget

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.animComposable(
    route: String,
    delay: Int = 700,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {

    composable(route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(delay)
            )

        },
        exitTransition = {

            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(delay)
            )

        },
        popEnterTransition = {

            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(delay)
            )


        },
        popExitTransition = {

            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(delay)
            )


        }) {
        content(it)
    }

}