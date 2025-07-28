package etf.ri.rma.newsfeedapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8C9EFF),       // blaga svetloplava (indigo accent)
    onPrimary = Color.Black,
    background = Color(0xFF121212),    // klasična tamna pozadina
    onBackground = Color(0xFFE0E0E0),  // svijetlo-siva za tekst na tamnoj pozadini
    surface = Color(0xFF1E1E2F),       // tamnija površina sa malo ljubičastog tona
    onSurface = Color(0xFFE0E0E0)      // svijetla boja teksta na površini
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),          // plava - možeš mijenjati po želji
    secondary = Color(0xFF64B5F6),        // svijetlija plava
    tertiary = Color(0xFF90CAF9),         // još svijetlija plava

    background = Color(0xFFB3E5FC),       // glatka svijetloplava pozadina
    surface = Color(0xFFE1F5FE),          // još svjetlija plava za površine
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color(0xFF0D47A1),     // tamnoplava za tekst na pozadini
    onSurface = Color(0xFF0D47A1)
)


@Composable
fun NewsFeedAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}