package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val handle: String = "",
    val onboardingComplete: Boolean = false,
    val vibePreset: String = "CYBER_GLOW",
    val fontStyle: String = "SLEEK_FUTURISTIC",
    val textSize: String = "MEDIUM",
    val accentColorHex: String = "#00FFCC",
    val cardStyle: String = "GLASSMORPHIC",
    val themeMode: String = "DARK",
    val backgroundPresetId: Int = 4
)

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val content: String,
    val imagePath: String? = null, // Path to local sketch drawing, preset note illustration, or camera snap
    val isPublic: Boolean = true,
    val authorHandle: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val friendHandle: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderHandle: String,
    val receiverHandle: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isDisappeared: Boolean = false,
    val viewed: Boolean = false // Used to trigger snap-style disappearing logic
)
