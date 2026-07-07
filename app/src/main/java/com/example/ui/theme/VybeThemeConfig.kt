package com.example.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile

enum class VibePreset {
    DARK_ACADEMIA,
    CYBER_GLOW,
    PASTEL_DREAMCORE,
    GRUNGE,
    Y2K,
    MINIMAL_MONO,
    CUSTOM
}

enum class FontStylePreset {
    ROUNDED_PLAYFUL,
    SLEEK_FUTURISTIC,
    HANDWRITTEN
}

enum class TextSizePreset {
    SMALL,
    MEDIUM,
    LARGE
}

enum class CardStylePreset {
    GLASSMORPHIC,
    NEON_OUTLINE,
    SOFT_SHADOW
}

enum class ThemeModePreset {
    LIGHT,
    DARK,
    AMOLED_BLACK
}

data class VybeThemeConfig(
    val preset: VibePreset = VibePreset.CYBER_GLOW,
    val fontStyle: FontStylePreset = FontStylePreset.SLEEK_FUTURISTIC,
    val textSize: TextSizePreset = TextSizePreset.MEDIUM,
    val accentColorHex: String = "#00FFCC",
    val cardStyle: CardStylePreset = CardStylePreset.GLASSMORPHIC,
    val themeMode: ThemeModePreset = ThemeModePreset.DARK,
    val backgroundPresetId: Int = 4
) {
    // Get colors
    val accentColor: Color
        get() = try {
            Color(android.graphics.Color.parseColor(accentColorHex))
        } catch (e: Exception) {
            Color(0xFF00FFCC) // fallback cyan
        }

    val backgroundColor: Color
        get() = when (themeMode) {
            ThemeModePreset.LIGHT -> when (preset) {
                VibePreset.PASTEL_DREAMCORE -> Color(0xFFFBF4FF)
                VibePreset.Y2K -> Color(0xFFE6F0FF)
                VibePreset.MINIMAL_MONO -> Color(0xFFFAFAFA)
                else -> Color(0xFFF9F9FB)
            }
            ThemeModePreset.DARK -> when (preset) {
                VibePreset.DARK_ACADEMIA -> Color(0xFF16110E)
                VibePreset.CYBER_GLOW -> Color(0xFF050508)
                VibePreset.GRUNGE -> Color(0xFF111412)
                VibePreset.Y2K -> Color(0xFF10101C)
                VibePreset.MINIMAL_MONO -> Color(0xFF121212)
                else -> Color(0xFF0D0D12)
            }
            ThemeModePreset.AMOLED_BLACK -> Color(0xFF000000)
        }

    val surfaceColor: Color
        get() = when (themeMode) {
            ThemeModePreset.LIGHT -> backgroundColor.copy(alpha = 0.85f)
            ThemeModePreset.DARK -> backgroundColor.copy(alpha = 0.85f)
            ThemeModePreset.AMOLED_BLACK -> Color(0xFF101010).copy(alpha = 0.85f)
        }

    val onBackgroundColor: Color
        get() = when (themeMode) {
            ThemeModePreset.LIGHT -> Color(0xFF1E1E24)
            ThemeModePreset.DARK, ThemeModePreset.AMOLED_BLACK -> Color(0xFFF1F1F6)
        }

    val onSurfaceColor: Color
        get() = onBackgroundColor

    // Typography styles
    val fontFamily: FontFamily
        get() = when (fontStyle) {
            FontStylePreset.ROUNDED_PLAYFUL -> FontFamily.SansSerif
            FontStylePreset.SLEEK_FUTURISTIC -> FontFamily.Monospace
            FontStylePreset.HANDWRITTEN -> FontFamily.Serif
        }

    val bodyFontSize: TextUnit
        get() = when (textSize) {
            TextSizePreset.SMALL -> 13.sp
            TextSizePreset.MEDIUM -> 15.sp
            TextSizePreset.LARGE -> 18.sp
        }

    val titleFontSize: TextUnit
        get() = when (textSize) {
            TextSizePreset.SMALL -> 18.sp
            TextSizePreset.MEDIUM -> 21.sp
            TextSizePreset.LARGE -> 25.sp
        }

    val headingFontSize: TextUnit
        get() = when (textSize) {
            TextSizePreset.SMALL -> 22.sp
            TextSizePreset.MEDIUM -> 26.sp
            TextSizePreset.LARGE -> 32.sp
        }

    val cardCornerRadius: Dp
        get() = when (preset) {
            VibePreset.DARK_ACADEMIA -> 4.dp
            VibePreset.CYBER_GLOW -> 14.dp
            VibePreset.PASTEL_DREAMCORE -> 28.dp
            VibePreset.GRUNGE -> 2.dp
            VibePreset.Y2K -> 18.dp
            VibePreset.MINIMAL_MONO -> 0.dp
            else -> 12.dp
        }

    companion object {
        fun fromProfile(profile: UserProfile): VybeThemeConfig {
            return VybeThemeConfig(
                preset = safeValueOf(profile.vibePreset, VibePreset.CYBER_GLOW),
                fontStyle = safeValueOf(profile.fontStyle, FontStylePreset.SLEEK_FUTURISTIC),
                textSize = safeValueOf(profile.textSize, TextSizePreset.MEDIUM),
                accentColorHex = profile.accentColorHex,
                cardStyle = safeValueOf(profile.cardStyle, CardStylePreset.GLASSMORPHIC),
                themeMode = safeValueOf(profile.themeMode, ThemeModePreset.DARK),
                backgroundPresetId = profile.backgroundPresetId
            )
        }

        private inline fun <reified T : Enum<T>> safeValueOf(name: String, fallback: T): T {
            return try {
                java.lang.Enum.valueOf(T::class.java, name)
            } catch (e: Exception) {
                fallback
            }
        }

        // Get default theme details for presets
        fun getPresetConfig(preset: VibePreset): VybeThemeConfig {
            return when (preset) {
                VibePreset.DARK_ACADEMIA -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.HANDWRITTEN,
                    textSize = TextSizePreset.MEDIUM,
                    accentColorHex = "#D4AF37", // Gold
                    cardStyle = CardStylePreset.SOFT_SHADOW,
                    themeMode = ThemeModePreset.DARK,
                    backgroundPresetId = 82
                )
                VibePreset.CYBER_GLOW -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.SLEEK_FUTURISTIC,
                    textSize = TextSizePreset.MEDIUM,
                    accentColorHex = "#00FFCC", // Neon Cyan
                    cardStyle = CardStylePreset.GLASSMORPHIC,
                    themeMode = ThemeModePreset.DARK,
                    backgroundPresetId = 4
                )
                VibePreset.PASTEL_DREAMCORE -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.ROUNDED_PLAYFUL,
                    textSize = TextSizePreset.LARGE,
                    accentColorHex = "#FFAEC9", // Pastel Pink
                    cardStyle = CardStylePreset.GLASSMORPHIC,
                    themeMode = ThemeModePreset.LIGHT,
                    backgroundPresetId = 27
                )
                VibePreset.GRUNGE -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.SLEEK_FUTURISTIC,
                    textSize = TextSizePreset.SMALL,
                    accentColorHex = "#C1F80A", // Acid Yellow-Green
                    cardStyle = CardStylePreset.NEON_OUTLINE,
                    themeMode = ThemeModePreset.DARK,
                    backgroundPresetId = 63
                )
                VibePreset.Y2K -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.ROUNDED_PLAYFUL,
                    textSize = TextSizePreset.MEDIUM,
                    accentColorHex = "#FF007F", // Neon Magenta
                    cardStyle = CardStylePreset.GLASSMORPHIC,
                    themeMode = ThemeModePreset.LIGHT,
                    backgroundPresetId = 3
                )
                VibePreset.MINIMAL_MONO -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.SLEEK_FUTURISTIC,
                    textSize = TextSizePreset.MEDIUM,
                    accentColorHex = "#000000", // Stark Black
                    cardStyle = CardStylePreset.SOFT_SHADOW,
                    themeMode = ThemeModePreset.LIGHT,
                    backgroundPresetId = 43
                )
                VibePreset.CUSTOM -> VybeThemeConfig(
                    preset = preset,
                    fontStyle = FontStylePreset.SLEEK_FUTURISTIC,
                    textSize = TextSizePreset.MEDIUM,
                    accentColorHex = "#00FFCC",
                    cardStyle = CardStylePreset.GLASSMORPHIC,
                    themeMode = ThemeModePreset.DARK,
                    backgroundPresetId = 4
                )
            }
        }
    }
}

val LocalVybeTheme = staticCompositionLocalOf { VybeThemeConfig() }
