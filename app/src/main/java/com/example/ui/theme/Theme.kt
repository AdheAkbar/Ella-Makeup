package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme =
  darkColorScheme(
      primary = Rose500,
      onPrimary = BgLight,
      primaryContainer = Rose600,
      onPrimaryContainer = Rose50,
      secondary = Zinc500,
      tertiary = Amber600,
      background = BgDark,
      surface = BgDark,
      surfaceVariant = Zinc800,
      onSurface = BgLight,
      onSurfaceVariant = Zinc400,
      error = Rose600,
      errorContainer = Rose600,
      onErrorContainer = Rose50
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Rose500,
    onPrimary = BgLight,
    primaryContainer = Rose50,
    onPrimaryContainer = Rose600,
    secondary = Zinc800,
    tertiary = Amber600,
    background = BgLight,
    surface = BgLight,
    surfaceVariant = BgLight,
    onBackground = Zinc900,
    onSurface = Zinc900,
    onSurfaceVariant = Zinc500,
    error = Rose600,
    errorContainer = Rose50,
    onErrorContainer = Rose600
  )

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled to enforce branding
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, shapes = Shapes, content = content)
}
