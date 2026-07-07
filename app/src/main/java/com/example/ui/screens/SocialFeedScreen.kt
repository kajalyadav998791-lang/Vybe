package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import com.example.ui.components.AnimateEntrance
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Friend
import com.example.data.Note
import com.example.ui.components.SubjectTag
import com.example.ui.components.VybeButton
import com.example.ui.components.VybeCard
import com.example.ui.components.VybeScreenBackground
import com.example.ui.components.VybeText
import com.example.ui.theme.LocalVybeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SocialFeedScreen(
    friends: List<Friend>,
    publicNotes: List<Note>,
    myNotes: List<Note>,
    onAddFriend: (String) -> Unit,
    onRemoveFriend: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onViewNoteDetails: (Note) -> Unit,
    onExportPdf: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalVybeTheme.current
    var friendSearchText by remember { mutableStateOf("") }
    var feedFilterFriendsOnly by remember { mutableStateOf(false) }

    // Combine friend notes and public notes
    val friendHandles = friends.map { it.friendHandle }.toSet()
    val filteredNotes = remember(publicNotes, myNotes, friendHandles, feedFilterFriendsOnly) {
        val allAvailable = (publicNotes + myNotes).distinctBy { it.id }
        if (feedFilterFriendsOnly) {
            allAvailable.filter { friendHandles.contains(it.authorHandle) }
        } else {
            allAvailable
        }
    }

    VybeScreenBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Social Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VybeText(
                    text = "STUDY FEED",
                    style = TextStyle(
                        fontSize = theme.headingFontSize,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    ),
                    color = theme.accentColor
                )

                // Filter toggle button
                IconButton(
                    onClick = { feedFilterFriendsOnly = !feedFilterFriendsOnly },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (feedFilterFriendsOnly) theme.accentColor.copy(alpha = 0.2f) else Color.Transparent)
                        .border(1.dp, if (feedFilterFriendsOnly) theme.accentColor else Color.Transparent, CircleShape)
                ) {
                    Icon(
                        imageVector = if (feedFilterFriendsOnly) Icons.Default.People else Icons.Default.Public,
                        contentDescription = "Toggle Feed Filter",
                        tint = if (feedFilterFriendsOnly) theme.accentColor else theme.onBackgroundColor
                    )
                }
            }

            VybeText(
                text = if (feedFilterFriendsOnly) "Showing notes shared by friends only" else "Browse notes shared publicly by other creators",
                style = TextStyle(fontSize = 13.sp),
                color = theme.onBackgroundColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Add Friend Input Row
            VybeCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                borderWidth = 1.dp
            ) {
                VybeText(
                    text = "ADD CO-STUDENT BY HANDLE",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = theme.accentColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = friendSearchText,
                        onValueChange = { friendSearchText = it },
                        placeholder = { Text("e.g. calculus_wiz", fontSize = 13.sp, fontFamily = theme.fontFamily) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.AlternateEmail,
                                contentDescription = "Add friend",
                                tint = theme.accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("friend_handle_input"),
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
                            if (friendSearchText.trim().isNotEmpty()) {
                                onAddFriend(friendSearchText.trim())
                                friendSearchText = ""
                            }
                        },
                        enabled = friendSearchText.trim().isNotEmpty(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(theme.cardCornerRadius))
                            .background(if (friendSearchText.trim().isNotEmpty()) theme.accentColor else theme.onBackgroundColor.copy(alpha = 0.1f))
                            .testTag("add_friend_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Friend Button",
                            tint = if (friendSearchText.trim().isNotEmpty()) {
                                if (theme.accentColorHex == "#FFFFFF") Color.Black else Color.White
                            } else theme.onBackgroundColor.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Horizontal Friends list (study group)
            if (friends.isNotEmpty()) {
                VybeText(
                    text = "STUDY CO-LAB (FRIENDS)",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = theme.accentColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(friends) { friend ->
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(theme.surfaceColor)
                                .border(1.dp, theme.onBackgroundColor.copy(alpha = 0.1f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(theme.accentColor)
                            )

                            VybeText(
                                text = "@${friend.friendHandle}",
                                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            )

                            // Chat icon
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Chat with Friend",
                                tint = theme.accentColor,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onOpenChat(friend.friendHandle) }
                            )

                            // Delete friend button
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Friend",
                                tint = theme.onBackgroundColor.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onRemoveFriend(friend.friendHandle) }
                            )
                        }
                    }
                }
            }

            // Masonry Feed Section
            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = "Empty Notes",
                            tint = theme.accentColor.copy(alpha = 0.4f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        VybeText(
                            text = "No study notes in this feed yet.",
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                        VybeText(
                            text = "Add friends or share notes publicly to see them here!",
                            style = TextStyle(fontSize = 12.sp),
                            color = theme.onBackgroundColor.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalItemSpacing = 10.dp
                ) {
                    itemsIndexed(filteredNotes) { index, note ->
                        // Styled strictly with the VIEWER'S chosen vibe!
                        AnimateEntrance(index = index) {
                            VybeCard(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onViewNoteDetails(note) }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    SubjectTag(subject = note.subject)
                                    
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = "One tap export PDF",
                                        tint = theme.accentColor.copy(alpha = 0.8f),
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable { onExportPdf(note) }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                VybeText(
                                    text = note.title,
                                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                VybeText(
                                    text = note.content,
                                    style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                                    color = theme.onBackgroundColor.copy(alpha = 0.7f),
                                    maxLines = 5
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Author footer
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(theme.accentColor.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        VybeText(
                                            text = note.authorHandle.take(1).uppercase(),
                                            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                            color = theme.accentColor
                                        )
                                    }

                                    VybeText(
                                        text = "@${note.authorHandle}",
                                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        color = theme.onBackgroundColor.copy(alpha = 0.6f),
                                        maxLines = 1
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
