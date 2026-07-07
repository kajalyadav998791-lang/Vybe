package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.theme.LocalVybeTheme
import kotlinx.coroutines.delay

@Composable
fun DisappearingChatScreen(
    friendHandle: String,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit,
    onMarkViewed: (Int) -> Unit,
    onMarkDisappeared: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    var inputText by remember { mutableStateOf("") }

    // Map message IDs to active countdown timers so we don't restart them
    val activeCountdowns = remember { mutableStateMapOf<Int, Int>() }

    // Call back to delete messages whose countdown has completed
    LaunchedEffect(activeCountdowns.keys.toList()) {
        activeCountdowns.keys.toList().forEach { msgId ->
            if (activeCountdowns[msgId] == 0) {
                activeCountdowns.remove(msgId)
                onMarkDisappeared(msgId)
            }
        }
    }

    // Coroutine to handle countdown ticks
    LaunchedEffect(activeCountdowns.size) {
        while (activeCountdowns.isNotEmpty()) {
            delay(1000L)
            activeCountdowns.keys.toList().forEach { id ->
                val current = activeCountdowns[id]
                if (current != null) {
                    if (current <= 1) {
                        activeCountdowns[id] = 0
                    } else {
                        activeCountdowns[id] = current - 1
                    }
                }
            }
        }
    }

    VybeScreenBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = theme.accentColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(theme.accentColor)
                        )
                        VybeText(
                            text = "@$friendHandle",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    VybeText(
                        text = "Disappearing study group room",
                        style = TextStyle(fontSize = 11.sp),
                        color = theme.onBackgroundColor.copy(alpha = 0.5f)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Secure Chats",
                    tint = theme.accentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Divider(color = theme.onBackgroundColor.copy(alpha = 0.08f))

            // Chat Message List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = false
            ) {
                items(messages, key = { it.id }) { msg ->
                    val isMyMsg = msg.senderHandle != friendHandle
                    val isViewed = msg.viewed
                    val countdown = activeCountdowns[msg.id]

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMyMsg) Arrangement.End else Arrangement.Start
                    ) {
                        if (isMyMsg) {
                            // My Message: show text normally but styled beautifully
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(
                                        topStart = theme.cardCornerRadius,
                                        topEnd = theme.cardCornerRadius,
                                        bottomStart = theme.cardCornerRadius,
                                        bottomEnd = 0.dp
                                    ))
                                    .background(theme.accentColor.copy(alpha = 0.15f))
                                    .border(1.dp, theme.accentColor.copy(alpha = 0.3f), RoundedCornerShape(
                                        topStart = theme.cardCornerRadius,
                                        topEnd = theme.cardCornerRadius,
                                        bottomStart = theme.cardCornerRadius,
                                        bottomEnd = 0.dp
                                    ))
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .widthIn(max = 260.dp)
                            ) {
                                Column {
                                    VybeText(
                                        text = msg.messageText,
                                        style = TextStyle(fontSize = 14.sp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    VybeText(
                                        text = "Sent · Snap mode",
                                        style = TextStyle(fontSize = 9.sp),
                                        color = theme.onBackgroundColor.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        } else {
                            // Friend's Message: "Tap to Reveal" mechanics
                            if (!isViewed) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(
                                            topStart = theme.cardCornerRadius,
                                            topEnd = theme.cardCornerRadius,
                                            bottomStart = 0.dp,
                                            bottomEnd = theme.cardCornerRadius
                                        ))
                                        .background(theme.surfaceColor)
                                        .border(1.dp, theme.accentColor.copy(alpha = 0.4f), RoundedCornerShape(
                                            topStart = theme.cardCornerRadius,
                                            topEnd = theme.cardCornerRadius,
                                            bottomStart = 0.dp,
                                            bottomEnd = theme.cardCornerRadius
                                        ))
                                        .clickable {
                                            onMarkViewed(msg.id)
                                            // Start countdown
                                            activeCountdowns[msg.id] = 8 // 8 seconds to read
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .testTag("tap_to_reveal_${msg.id}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Hidden Message",
                                            tint = theme.accentColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        VybeText(
                                            text = "TAP TO REVEAL CO-LAB 👻",
                                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp),
                                            color = theme.accentColor
                                        )
                                    }
                                }
                            } else {
                                // Message is revealed, showing active countdown
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(
                                            topStart = theme.cardCornerRadius,
                                            topEnd = theme.cardCornerRadius,
                                            bottomStart = 0.dp,
                                            bottomEnd = theme.cardCornerRadius
                                        ))
                                        .background(theme.surfaceColor)
                                        .border(1.dp, theme.onBackgroundColor.copy(alpha = 0.1f), RoundedCornerShape(
                                            topStart = theme.cardCornerRadius,
                                            topEnd = theme.cardCornerRadius,
                                            bottomStart = 0.dp,
                                            bottomEnd = theme.cardCornerRadius
                                        ))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                        .widthIn(max = 260.dp)
                                ) {
                                    Column {
                                        VybeText(
                                            text = msg.messageText,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        
                                        // Countdown progress
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.Red)
                                            )
                                            VybeText(
                                                text = "Disappearing in ${countdown ?: 8}s...",
                                                style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                                color = Color.Red.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Input Panel
            VybeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                borderWidth = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask a quick study doubt...", fontSize = 13.sp, fontFamily = theme.fontFamily) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_text"),
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

                    IconButton(
                        onClick = {
                            if (inputText.trim().isNotEmpty()) {
                                onSendMessage(inputText.trim())
                                inputText = ""
                            }
                        },
                        enabled = inputText.trim().isNotEmpty(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(theme.cardCornerRadius))
                            .background(if (inputText.trim().isNotEmpty()) theme.accentColor else theme.onBackgroundColor.copy(alpha = 0.1f))
                            .testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Chat Message",
                            tint = if (inputText.trim().isNotEmpty()) {
                                if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White
                            } else theme.onBackgroundColor.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}
