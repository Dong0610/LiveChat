package dong.duan.livechat.ui.theme

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import dong.duan.lib.library.sharedPreferences
import dong.duan.livechat.AppContext
import dong.duan.livechat.R

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = Color.Black,
    tertiary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007DFE),
    secondary = Color.White,
    tertiary = Color.White
)

@Composable
fun LiveChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val isSignedIn = sharedPreferences.getBollean("KEY_SIGN", false)
            val backgroundResourceId =
                if (isSignedIn) R.drawable.default_status else R.drawable.gradient_horizontal
            val background = ContextCompat.getDrawable(AppContext.context, backgroundResourceId)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =
                ContextCompat.getColor(AppContext.context, android.R.color.transparent)
            window.setBackgroundDrawable(background)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isSignedIn
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
