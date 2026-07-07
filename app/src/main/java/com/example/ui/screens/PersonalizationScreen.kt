package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.ui.theme.BackgroundStylePresets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ColorPickerRow
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.components.AnimateEntrance
import com.example.ui.theme.*

@Composable
fun PersonalizationScreen(
    themeConfig: VybeThemeConfig,
    onPresetChanged: (VibePreset) -> Unit,
    onFontChanged: (FontStylePreset) -> Unit,
    onSizeChanged: (TextSizePreset) -> Unit,
    onAccentChanged: (String) -> Unit,
    onCardStyleChanged: (CardStylePreset) -> Unit,
    onThemeModeChanged: (ThemeModePreset) -> Unit,
    onBackgroundPresetChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    val animatedAccent by animateColorAsState(targetValue = theme.accentColor, label = "accentColor")

    VybeScreenBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Screen Header
            AnimateEntrance(index = 0) {
                Column {
                    VybeText(
                        text = "CUSTOMIZER",
                        style = TextStyle(
                            fontSize = theme.headingFontSize,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp
                        ),
                        color = animatedAccent
                    )
                    VybeText(
                        text = "Tweak your vybe. Applied instantly across the app.",
                        style = TextStyle(fontSize = 13.sp),
                        color = theme.onBackgroundColor.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }

            // Vibe Presets Selector (quick shortcuts)
            AnimateEntrance(index = 1) {
                Column {
                    VybeText(
                        text = "QUICK RESKINS",
                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            VibePreset.CYBER_GLOW to "Cyber",
                            VibePreset.DARK_ACADEMIA to "Academia",
                            VibePreset.PASTEL_DREAMCORE to "Pastel",
                            VibePreset.GRUNGE to "Grunge",
                            VibePreset.Y2K to "Y2K",
                            VibePreset.MINIMAL_MONO to "Mono"
                        ).forEach { (v, name) ->
                            val isSelected = themeConfig.preset == v
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) animatedAccent.copy(alpha = 0.2f) else theme.surfaceColor.copy(alpha = 0.3f))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onPresetChanged(v) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                VybeText(
                                    text = name,
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                    color = if (isSelected) animatedAccent else theme.onBackgroundColor
                                )
                            }
                        }
                    }
                }
            }

            // Customizable Options Panel
            AnimateEntrance(index = 2) {
                VybeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    // 1. Font Selection
                    VybeText(
                        text = "FONT STYLE",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            FontStylePreset.ROUNDED_PLAYFUL to "Rounded",
                            FontStylePreset.SLEEK_FUTURISTIC to "Sleek Tech",
                            FontStylePreset.HANDWRITTEN to "Handwritten"
                        ).forEach { (f, label) ->
                            val isSelected = themeConfig.fontStyle == f
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) animatedAccent.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onFontChanged(f) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontFamily = when (f) {
                                        FontStylePreset.ROUNDED_PLAYFUL -> FontFamily.SansSerif
                                        FontStylePreset.SLEEK_FUTURISTIC -> FontFamily.Monospace
                                        FontStylePreset.HANDWRITTEN -> FontFamily.Serif
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) animatedAccent else theme.onBackgroundColor
                                )
                            }
                        }
                    }

                    // 2. Text Sizes
                    VybeText(
                        text = "TEXT COMFORT SIZE",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            TextSizePreset.SMALL to "Small (13sp)",
                            TextSizePreset.MEDIUM to "Medium (15sp)",
                            TextSizePreset.LARGE to "Large (18sp)"
                        ).forEach { (sz, label) ->
                            val isSelected = themeConfig.textSize == sz
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) animatedAccent.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onSizeChanged(sz) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                VybeText(
                                    text = label,
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                    color = if (isSelected) animatedAccent else theme.onBackgroundColor
                                )
                            }
                        }
                    }

                    // 3. Accent Color Picker
                    VybeText(
                        text = "CUSTOM ACCENT COLOR",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    ColorPickerRow(
                        selectedHex = themeConfig.accentColorHex,
                        onColorSelected = onAccentChanged,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // 4. Card Borders and Shadows
                    VybeText(
                        text = "SURFACE CANVAS STYLE",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            CardStylePreset.GLASSMORPHIC to "Glassmorphic",
                            CardStylePreset.NEON_OUTLINE to "Neon Line",
                            CardStylePreset.SOFT_SHADOW to "Soft Depth"
                        ).forEach { (cs, label) ->
                            val isSelected = themeConfig.cardStyle == cs
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) animatedAccent.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onCardStyleChanged(cs) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                VybeText(
                                    text = label,
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                    color = if (isSelected) animatedAccent else theme.onBackgroundColor
                                )
                            }
                        }
                    }

                    // 5. Light/Dark/AMOLED-black Mode
                    VybeText(
                        text = "THEME AMBIENCE",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            ThemeModePreset.LIGHT to Icons.Default.LightMode,
                            ThemeModePreset.DARK to Icons.Default.DarkMode,
                            ThemeModePreset.AMOLED_BLACK to Icons.Default.BrightnessLow
                        ).forEach { (mode, icon) ->
                            val isSelected = themeConfig.themeMode == mode
                            val label = when (mode) {
                                ThemeModePreset.LIGHT -> "Light"
                                ThemeModePreset.DARK -> "Dark Core"
                                ThemeModePreset.AMOLED_BLACK -> "AMOLED Black"
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) animatedAccent.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onThemeModeChanged(mode) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (isSelected) animatedAccent else theme.onBackgroundColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    VybeText(
                                        text = label,
                                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                        color = if (isSelected) animatedAccent else theme.onBackgroundColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Aesthetic Background (100 options from Pinterest / Google style)
            AnimateEntrance(index = 3) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    VybeText(
                        text = "AESTHETIC WALLPAPERS & GRADIENTS (100 OPTIONS)",
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp),
                        color = animatedAccent,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    VybeText(
                        text = "Instantly source gorgeous backing skins from Pinterest & Unsplash aesthetics.",
                        style = TextStyle(fontSize = 11.sp),
                        color = theme.onBackgroundColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Categories Tab Selector
                    var selectedCategory by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("All") }
                    val categories = listOf("All", "Instagram Gradients", "Pinterest Softcores", "Tokyo Monochromes", "Cyberpunk Textures", "Dark Academia", "Aesthetic Moods")
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(categories.size) { index ->
                            val cat = categories[index]
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) animatedAccent else theme.surfaceColor.copy(alpha = 0.3f))
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                VybeText(
                                    text = when (cat) {
                                        "Tokyo Monochromes" -> "Tokyo Mono"
                                        "Pinterest Softcores" -> "Pinterest Pastel"
                                        "Instagram Gradients" -> "Sunset Glows"
                                        else -> cat
                                    },
                                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                    color = if (isSelected) (if (theme.accentColorHex == "#000000") Color.White else Color.Black) else theme.onBackgroundColor
                                )
                            }
                        }
                    }

                    // Display filtered background items in a horizontal scrollable row (shows beautiful thumbnails!)
                    val filteredPresets = androidx.compose.runtime.remember(selectedCategory) {
                        if (selectedCategory == "All") {
                            BackgroundStylePresets.presets
                        } else {
                            BackgroundStylePresets.presets.filter { it.category == selectedCategory }
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    ) {
                        items(filteredPresets.size) { index ->
                            val preset = filteredPresets[index]
                            val isActive = themeConfig.backgroundPresetId == preset.id
                            
                            Box(
                                modifier = Modifier
                                    .width(85.dp)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onBackgroundPresetChanged(preset.id) }
                                    .border(
                                        width = if (isActive) 3.dp else 1.dp,
                                        color = if (isActive) animatedAccent else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                if (preset.imageUrl != null) {
                                    // Image background preview using Coil
                                    AsyncImage(
                                        model = preset.imageUrl,
                                        contentDescription = preset.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Overlay for name legibility
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.45f))
                                    )
                                } else {
                                    // Gradient preview
                                    val brush = androidx.compose.runtime.remember(preset.colors) {
                                        if (preset.colors.size > 1) androidx.compose.ui.graphics.Brush.linearGradient(preset.colors)
                                        else androidx.compose.ui.graphics.Brush.linearGradient(listOf(preset.colors[0], theme.backgroundColor))
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(brush)
                                    ) {
                                        // Pattern overlay mini preview
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            val colorAlpha = if (preset.isDark) 0.15f else 0.25f
                                            val patternColor = (preset.colors.firstOrNull() ?: theme.accentColor).copy(alpha = colorAlpha)
                                            when (preset.patternType) {
                                                "dots" -> {
                                                    drawCircle(patternColor, 1.dp.toPx(), Offset(size.width * 0.3f, size.height * 0.3f))
                                                    drawCircle(patternColor, 1.dp.toPx(), Offset(size.width * 0.7f, size.height * 0.3f))
                                                    drawCircle(patternColor, 1.dp.toPx(), Offset(size.width * 0.3f, size.height * 0.7f))
                                                    drawCircle(patternColor, 1.dp.toPx(), Offset(size.width * 0.7f, size.height * 0.7f))
                                                }
                                                "grid" -> {
                                                    drawLine(patternColor, Offset(size.width * 0.5f, 0f), Offset(size.width * 0.5f, size.height))
                                                    drawLine(patternColor, Offset(0f, size.height * 0.5f), Offset(size.width, size.height * 0.5f))
                                                }
                                                "lines" -> {
                                                    drawLine(patternColor, Offset(0f, 0f), Offset(size.width, size.height))
                                                }
                                            }
                                        }
                                    }
                                }

                                // Preset meta details
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Simple number badge
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(Color.Black.copy(alpha = 0.5f))
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "#${preset.id}",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        if (isActive) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = animatedAccent,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = preset.name,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        lineHeight = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
