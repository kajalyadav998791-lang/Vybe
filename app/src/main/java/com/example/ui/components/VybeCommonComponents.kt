package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.ui.theme.BackgroundStylePresets
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun VybeScreenBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalVybeTheme.current
    val backgroundPreset = remember(theme.backgroundPresetId) {
        BackgroundStylePresets.presets.firstOrNull { it.id == theme.backgroundPresetId }
            ?: BackgroundStylePresets.presets.first()
    }
    
    val animatedAccentColor by animateColorAsState(targetValue = theme.accentColor, label = "accentColor")
    val animatedBgColor by animateColorAsState(targetValue = theme.backgroundColor, label = "bgColor")

    val gradientBrush = remember(backgroundPreset.colors, animatedBgColor) {
        val colors = backgroundPreset.colors
        if (colors.size > 1) {
            Brush.linearGradient(colors)
        } else if (colors.isNotEmpty()) {
            Brush.linearGradient(listOf(colors[0], animatedBgColor))
        } else {
            Brush.linearGradient(listOf(animatedBgColor, animatedBgColor))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (backgroundPreset.imageUrl == null) {
                    Modifier.background(gradientBrush)
                } else {
                    Modifier.background(animatedBgColor)
                }
            )
    ) {
        // Image Wallpaper if present
        if (backgroundPreset.imageUrl != null) {
            AsyncImage(
                model = backgroundPreset.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Legibility overlay scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (backgroundPreset.isDark) Color.Black.copy(alpha = 0.65f)
                        else Color.White.copy(alpha = 0.65f)
                    )
            )
        }

        // Overlay pattern drawing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val colorAlpha = if (backgroundPreset.isDark) 0.08f else 0.12f
                    val patternColor = animatedAccentColor.copy(alpha = colorAlpha)

                    when (backgroundPreset.patternType) {
                        "dots" -> {
                            val dotRadius = 1.5.dp.toPx()
                            val spacing = 20.dp.toPx()
                            for (x in 0..size.width.toInt() step spacing.toInt()) {
                                for (y in 0..size.height.toInt() step spacing.toInt()) {
                                    drawCircle(
                                        color = patternColor,
                                        radius = dotRadius,
                                        center = Offset(x.toFloat(), y.toFloat())
                                    )
                                }
                            }
                        }
                        "grid" -> {
                            val spacing = 30.dp.toPx()
                            for (x in 0..size.width.toInt() step spacing.toInt()) {
                                drawLine(
                                    color = patternColor.copy(alpha = colorAlpha * 0.7f),
                                    start = Offset(x.toFloat(), 0f),
                                    end = Offset(x.toFloat(), size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            for (y in 0..size.height.toInt() step spacing.toInt()) {
                                drawLine(
                                    color = patternColor.copy(alpha = colorAlpha * 0.7f),
                                    start = Offset(0f, y.toFloat()),
                                    end = Offset(size.width, y.toFloat()),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                        }
                        "lines" -> {
                            val spacing = 24.dp.toPx()
                            for (offset in 0..(size.width + size.height).toInt() step spacing.toInt()) {
                                drawLine(
                                    color = patternColor.copy(alpha = colorAlpha * 0.5f),
                                    start = Offset(offset.toFloat(), 0f),
                                    end = Offset((offset - size.height).toFloat(), size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                        }
                    }

                    // Add dynamic floating visual glow aura (always looks incredible)
                    if (theme.preset != VibePreset.MINIMAL_MONO) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    animatedAccentColor.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            radius = size.width * 0.8f,
                            center = Offset(size.width * 0.9f, size.height * 0.1f)
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    animatedAccentColor.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            ),
                            radius = size.width * 0.9f,
                            center = Offset(size.width * 0.1f, size.height * 0.8f)
                        )
                    }
                }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun VybeCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    borderWidth: Dp = 1.dp,
    testTag: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalVybeTheme.current
    val cardRadius by animateDpAsState(targetValue = theme.cardCornerRadius, label = "cardRadius")
    val shape = RoundedCornerShape(cardRadius)

    val animatedAccent by animateColorAsState(targetValue = theme.accentColor, label = "accentColor")
    val animatedSurface by animateColorAsState(targetValue = theme.surfaceColor, label = "surfaceColor")
    val animatedOnSurface by animateColorAsState(targetValue = theme.onSurfaceColor, label = "onSurfaceColor")

    val baseModifier = modifier
        .clip(shape)
        .then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else Modifier
        )
        .then(
            if (testTag != null) Modifier.testTag(testTag) else Modifier
        )

    val styledModifier = when (theme.cardStyle) {
        CardStylePreset.GLASSMORPHIC -> {
            baseModifier
                .background(animatedSurface.copy(alpha = 0.45f))
                .border(
                    width = borderWidth,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            animatedAccent.copy(alpha = 0.18f)
                        )
                    ),
                    shape = shape
                )
                .shadow(
                    elevation = 6.dp,
                    shape = shape,
                    ambientColor = animatedAccent.copy(alpha = 0.15f),
                    spotColor = animatedAccent.copy(alpha = 0.25f)
                )
        }
        CardStylePreset.NEON_OUTLINE -> {
            baseModifier
                .background(animatedSurface)
                .border(
                    width = borderWidth + 1.dp,
                    color = animatedAccent,
                    shape = shape
                )
        }
        CardStylePreset.SOFT_SHADOW -> {
            baseModifier
                .background(animatedSurface)
                .shadow(
                    elevation = 8.dp,
                    shape = shape,
                    ambientColor = Color.Black.copy(alpha = 0.18f),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .border(
                    width = borderWidth,
                    color = animatedOnSurface.copy(alpha = 0.08f),
                    shape = shape
                )
        }
    }

    Column(
        modifier = styledModifier.padding(16.dp),
        content = content
    )
}

@Composable
fun VybeText(
    text: String,
    style: TextStyle = TextStyle.Default,
    color: Color = LocalVybeTheme.current.onBackgroundColor,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    val theme = LocalVybeTheme.current
    val animatedColor by animateColorAsState(targetValue = color, label = "textColor")

    Text(
        text = text,
        style = style.copy(
            fontFamily = theme.fontFamily,
            fontWeight = fontWeight ?: style.fontWeight,
            color = animatedColor
        ),
        modifier = modifier,
        maxLines = maxLines
    )
}

@Composable
fun VybeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    testTag: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    val theme = LocalVybeTheme.current
    val animatedAccent by animateColorAsState(targetValue = theme.accentColor, label = "buttonAccent")
    val cardRadius by animateDpAsState(targetValue = theme.cardCornerRadius, label = "buttonRadius")

    val buttonShape = RoundedCornerShape(cardRadius)

    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        enabled = enabled,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedAccent,
            contentColor = if (theme.accentColorHex == "#FFFFFF" || theme.preset == VibePreset.MINIMAL_MONO) Color.Black else Color.White,
            disabledContainerColor = animatedAccent.copy(alpha = 0.4f),
            disabledContentColor = Color.Gray
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (theme.cardStyle == CardStylePreset.SOFT_SHADOW) 6.dp else 0.dp,
            pressedElevation = 2.dp
        ),
        content = content
    )
}

@Composable
fun SubjectTag(
    subject: String,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    val animatedAccent by animateColorAsState(targetValue = theme.accentColor, label = "tagAccent")

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(animatedAccent.copy(alpha = 0.15f))
            .border(1.dp, animatedAccent.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        VybeText(
            text = subject.uppercase(),
            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
            color = animatedAccent
        )
    }
}

@Composable
fun ColorPickerRow(
    selectedHex: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        "#00FFCC", // Cyber Cyan
        "#FF007F", // Neon Magenta
        "#C1F80A", // Acid Lime
        "#D4AF37", // Gold
        "#3B82F6", // Aesthetic Blue
        "#EF4444", // Crimson Red
        "#10B981", // Emerald Green
        "#8B5CF6", // Lavender Purple
        "#FF7A00"  // Blaze Orange
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { hex ->
            val color = Color(android.graphics.Color.parseColor(hex))
            val isSelected = selectedHex.equals(hex, ignoreCase = true)

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(hex) }
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun AnimateEntrance(
    index: Int = 0,
    staggerDelay: Int = 50,
    initialOffsetY: Float = 40f,
    initialScale: Float = 0.93f,
    content: @Composable () -> Unit
) {
    // We animate three properties: alpha, translationY, and scale
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(initialOffsetY) }
    val scale = remember { Animatable(initialScale) }

    LaunchedEffect(key1 = Unit) {
        if (index > 0) {
            delay((index * staggerDelay).toLong())
        }
        coroutineScope {
            launch {
                alpha.animateTo(1f, spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium))
            }
            launch {
                offsetY.animateTo(0f, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow))
            }
            launch {
                scale.animateTo(1f, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow))
            }
        }
    }

    Box(
        modifier = Modifier.graphicsLayer(
            alpha = alpha.value,
            translationY = offsetY.value,
            scaleX = scale.value,
            scaleY = scale.value
        )
    ) {
        content()
    }
}

