package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VybeButton
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.theme.LocalVybeTheme
import com.example.ui.theme.VibePreset
import com.example.ui.theme.VybeThemeConfig

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    currentThemeConfig: VybeThemeConfig,
    onVibeSelected: (VibePreset) -> Unit,
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var handle by remember { mutableStateOf("") }
    val theme = LocalVybeTheme.current

    val vibes = listOf(
        VibePreset.CYBER_GLOW to "CYBER-GLOW",
        VibePreset.DARK_ACADEMIA to "DARK ACADEMIA",
        VibePreset.PASTEL_DREAMCORE to "PASTEL DREAM",
        VibePreset.GRUNGE to "GRUNGE",
        VibePreset.Y2K to "Y2K",
        VibePreset.MINIMAL_MONO to "MINIMAL MONO"
    )

    VybeScreenBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Title
            VybeText(
                text = "VYBE",
                style = TextStyle(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp
                ),
                color = theme.accentColor
            )

            VybeText(
                text = "Aesthetic note sharing for the next gen.",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                color = theme.onBackgroundColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Handle Input Card
            VybeCard(
                modifier = Modifier.fillMaxWidth(),
                borderWidth = 1.dp
            ) {
                VybeText(
                    text = "Username",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    placeholder = { Text("Choose a username", fontFamily = theme.fontFamily) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AlternateEmail,
                            contentDescription = "Handle symbol",
                            tint = theme.accentColor
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input"),
                    shape = RoundedCornerShape(theme.cardCornerRadius),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = theme.onBackgroundColor,
                        unfocusedTextColor = theme.onBackgroundColor,
                        focusedBorderColor = theme.accentColor,
                        unfocusedBorderColor = theme.onBackgroundColor.copy(alpha = 0.2f),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Grid subtitle
            VybeText(
                text = "TAP TO INSTANTLY RE-SKIN",
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                color = theme.accentColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Staggered Masonry Grid of Vibe Presets
            Box(modifier = Modifier.weight(1f)) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp
                ) {
                    items(vibes) { (preset, label) ->
                        val presetConfig = VybeThemeConfig.getPresetConfig(preset)
                        val isSelected = currentThemeConfig.preset == preset

                        // Visual preview of the vibe inside its card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (preset == VibePreset.CYBER_GLOW || preset == VibePreset.GRUNGE) 130.dp else 100.dp)
                                .clip(RoundedCornerShape(presetConfig.cardCornerRadius))
                                .background(presetConfig.backgroundColor)
                                .clickable { onVibeSelected(preset) }
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) theme.accentColor else presetConfig.accentColor.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(presetConfig.cardCornerRadius)
                                )
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Little aesthetic color orb
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(presetConfig.accentColor)
                                    )
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(theme.accentColor)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "ACTIVE",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (theme.accentColorHex == "#000000") Color.White else Color.Black
                                            )
                                        }
                                    }
                                }

                                Column {
                                    Text(
                                        text = label,
                                        fontFamily = presetConfig.fontFamily,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        color = presetConfig.onBackgroundColor
                                    )
                                    Text(
                                        text = "Vibe Code: ${preset.ordinal}",
                                        fontFamily = presetConfig.fontFamily,
                                        fontSize = 9.sp,
                                        color = presetConfig.onBackgroundColor.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Button
            VybeButton(
                onClick = {
                    if (handle.trim().isNotEmpty()) {
                        onComplete(handle.trim())
                    }
                },
                enabled = handle.trim().isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                testTag = "submit_button"
            ) {
                VybeText(
                    text = "LFG  ➔",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    color = if (theme.accentColorHex == "#FFFFFF" || theme.preset == VibePreset.MINIMAL_MONO) Color.Black else Color.White
                )
            }
        }
    }
}
