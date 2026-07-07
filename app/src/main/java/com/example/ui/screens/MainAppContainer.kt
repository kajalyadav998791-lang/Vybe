package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Note
import com.example.ui.VybeViewModel
import com.example.ui.components.SubjectTag
import com.example.ui.components.VybeButton
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.theme.LocalVybeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(
    viewModel: VybeViewModel,
    profile: com.example.data.UserProfile?,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    val context = LocalContext.current

    // Observe flows
    val allNotes by viewModel.allNotes.collectAsState()
    val publicNotes by viewModel.publicNotes.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val activeFriend by viewModel.activeChatFriend.collectAsState()
    val chatMessages by viewModel.activeChatMessages.collectAsState()

    // UI Navigation State
    var currentTab by remember { mutableStateOf("feed") }
    var detailedNote by remember { mutableStateOf<Note?>(null) }

    // If profile is not complete, show onboarding
    if (profile == null || !profile.onboardingComplete) {
        OnboardingScreen(
            currentThemeConfig = theme,
            onVibeSelected = { viewModel.updateVibePreset(it) },
            onComplete = { handle ->
                viewModel.completeOnboarding(handle, theme.preset)
            }
        )
        return
    }

    // Direct active chat room overlay
    if (activeFriend != null) {
        DisappearingChatScreen(
            friendHandle = activeFriend!!,
            messages = chatMessages,
            onSendMessage = { text -> viewModel.sendChatMessage(text) },
            onBack = {
                // Clear viewed messages so they disappear on close
                viewModel.clearDisappearedMessagesOfActiveFriend()
                viewModel.activeChatFriend.value = null
            },
            onMarkViewed = { msgId -> viewModel.markMessageAsViewed(msgId) },
            onMarkDisappeared = { msgId -> viewModel.markMessageAsDisappeared(msgId) }
        )
        return
    }

    // Main scaffold navigation
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                // Customized Bottom Navigation Capsule
                val navigationRadius by animateDpAsState(targetValue = theme.cardCornerRadius, label = "navRadius")
                val navShape = RoundedCornerShape(navigationRadius)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(navShape)
                        .background(theme.surfaceColor)
                        .border(1.dp, theme.accentColor.copy(alpha = 0.2f), navShape)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val tabs = listOf(
                        Triple("feed", Icons.Default.MenuBook, "Feed"),
                        Triple("notes", Icons.Default.EditNote, "My Lab"),
                        Triple("customize", Icons.Default.Palette, "Customizer")
                    )

                    tabs.forEach { (tabId, icon, label) ->
                        val isSelected = currentTab == tabId
                        val activeColor = theme.accentColor
                        val inactiveColor = theme.onBackgroundColor.copy(alpha = 0.4f)

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { currentTab = tabId }
                                .testTag("nav_tab_$tabId")
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) activeColor else inactiveColor,
                                modifier = Modifier.size(if (isSelected) 24.dp else 20.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            VybeText(
                                text = label,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) activeColor else inactiveColor
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                "feed" -> {
                    val myNotesFiltered = allNotes.filter { it.authorHandle == profile.handle }
                    SocialFeedScreen(
                        friends = friends,
                        publicNotes = publicNotes,
                        myNotes = myNotesFiltered,
                        onAddFriend = { viewModel.addFriend(it) },
                        onRemoveFriend = { viewModel.removeFriend(it) },
                        onOpenChat = { viewModel.activeChatFriend.value = it },
                        onViewNoteDetails = { detailedNote = it },
                        onExportPdf = { note -> exportNoteToPdf(context, note) }
                    )
                }
                "notes" -> {
                    val myNotesFiltered = allNotes.filter { it.authorHandle == profile.handle }
                    NotesScreen(
                        myNotes = myNotesFiltered,
                        onAddNote = { title, sub, content, pub, img ->
                            viewModel.addNote(title, sub, content, pub, img)
                        },
                        onDeleteNote = { id -> viewModel.deleteNote(id) },
                        onExportPdf = { note -> exportNoteToPdf(context, note) }
                    )
                }
                "customize" -> {
                    PersonalizationScreen(
                        themeConfig = theme,
                        onPresetChanged = { viewModel.updateVibePreset(it) },
                        onFontChanged = { viewModel.updateFontStyle(it) },
                        onSizeChanged = { viewModel.updateTextSize(it) },
                        onAccentChanged = { viewModel.updateAccentColor(it) },
                        onCardStyleChanged = { viewModel.updateCardStyle(it) },
                        onThemeModeChanged = { viewModel.updateThemeMode(it) },
                        onBackgroundPresetChanged = { viewModel.updateBackgroundPreset(it) }
                    )
                }
            }
        }
    }

    // Modal Sheet or Alert Dialog to read note details fully
    detailedNote?.let { note ->
        AlertDialog(
            onDismissRequest = { detailedNote = null },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            shape = RoundedCornerShape(theme.cardCornerRadius),
            containerColor = theme.surfaceColor,
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SubjectTag(subject = note.subject)
                        IconButton(onClick = { detailedNote = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = theme.onBackgroundColor.copy(alpha = 0.5f))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    VybeText(
                        text = note.title,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Black),
                        color = theme.accentColor
                    )
                    VybeText(
                        text = "Shared by @${note.authorHandle}",
                        style = TextStyle(fontSize = 11.sp),
                        color = theme.onBackgroundColor.copy(alpha = 0.5f)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VybeText(
                        text = note.content,
                        style = TextStyle(fontSize = 14.sp, lineHeight = 20.sp)
                    )

                    if (note.imagePath != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(theme.cardCornerRadius))
                                .background(theme.onBackgroundColor.copy(alpha = 0.05f))
                                .border(1.dp, theme.onBackgroundColor.copy(alpha = 0.1f), RoundedCornerShape(theme.cardCornerRadius)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Gesture, contentDescription = "Sketch", tint = theme.accentColor, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                VybeText(text = "Doodle Attached", style = TextStyle(fontSize = 11.sp), color = theme.onBackgroundColor.copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Check if author is not me and not already added
                    val friendHandles = friends.map { it.friendHandle }
                    val isMyNote = note.authorHandle == profile.handle
                    val isAlreadyFriend = friendHandles.contains(note.authorHandle)

                    if (!isMyNote && !isAlreadyFriend) {
                        VybeButton(
                            onClick = {
                                viewModel.addFriend(note.authorHandle)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            VybeText(
                                text = "ADD @${note.authorHandle}",
                                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                color = if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White
                            )
                        }
                    }

                    VybeButton(
                        onClick = {
                            exportNoteToPdf(context, note)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White, modifier = Modifier.size(16.dp))
                            VybeText(
                                text = "PDF EXPORT",
                                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                color = if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White
                            )
                        }
                    }
                }
            }
        )
    }
}
