package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class VybeViewModel(private val repository: VybeRepository) : ViewModel() {

    // Observe active profile
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Derived theme configuration state
    val themeConfig: StateFlow<VybeThemeConfig> = userProfile
        .map { profile ->
            if (profile != null) VybeThemeConfig.fromProfile(profile) else VybeThemeConfig()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VybeThemeConfig())

    // All notes and public notes
    val allNotes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publicNotes: StateFlow<List<Note>> = repository.publicNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Friends list
    val friends: StateFlow<List<Friend>> = repository.friends
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active friend for direct messaging
    val activeChatFriend = MutableStateFlow<String?>(null)

    // Flow for current active conversation history
    val activeChatMessages: StateFlow<List<ChatMessage>> = combine(
        userProfile.filterNotNull(),
        activeChatFriend.filterNotNull()
    ) { profile, friend ->
        profile.handle to friend
    }.flatMapLatest { (myHandle, friendHandle) ->
        repository.getChatHistoryFlow(myHandle, friendHandle)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        seedDatabaseIfNeeded()
    }

    private fun seedDatabaseIfNeeded() {
        viewModelScope.launch {
            val profile = repository.getUserProfile()
            if (profile == null) {
                // Initialize default profile
                val defaultProfile = UserProfile(
                    id = 1,
                    handle = "vibe_pioneer",
                    onboardingComplete = false,
                    vibePreset = "CYBER_GLOW",
                    fontStyle = "SLEEK_FUTURISTIC",
                    textSize = "MEDIUM",
                    accentColorHex = "#00FFCC",
                    cardStyle = "GLASSMORPHIC",
                    themeMode = "DARK"
                )
                repository.saveUserProfile(defaultProfile)

                // Initialize beautiful default public notes
                repository.insertNote(Note(
                    title = "Calculus Limits Cheat Sheet",
                    subject = "Mathematics",
                    content = "Quick check: limit of sin(x)/x as x approaches 0 is 1.\n\nL'Hopital's Rule applies when you face indeterminate forms of type 0/0 or inf/inf. Differentiate the top and bottom separately!\n\nOther Derivatives to know:\n- d/dx(e^x) = e^x\n- d/dx(ln x) = 1/x\n- d/dx(sin x) = cos x\n- d/dx(cos x) = -sin x\n\nStudy math in Cyber-Glow for ultimate focus!",
                    isPublic = true,
                    authorHandle = "calculus_wiz"
                ))

                repository.insertNote(Note(
                    title = "Organic Chem Benzene Rings",
                    subject = "Chemistry",
                    content = "Benzene rings are flat, planar, and have exceptional resonance stability due to 6 delocalized pi electrons!\n\nPrimary Electrophilic Aromatic Substitution (EAS) mechanisms:\n1. Nitration (HNO3 / H2SO4) -> Nitrobenzene\n2. Halogenation (Br2 / FeBr3) -> Bromobenzene\n3. Friedel-Crafts Acylation (Acid chloride + AlCl3)\n\nTip: draw neat hexagonal shapes in exams!",
                    isPublic = true,
                    authorHandle = "chem_girl_09"
                ))

                repository.insertNote(Note(
                    title = "The Great Gatsby Symbolisms",
                    subject = "English Literature",
                    content = "Fitzgerald's key modern elements in deconstruction:\n\n1. The Green Light: Gatsby's ultimate dream, hopes, and Daisy's illusion.\n2. The Valley of Ashes: The social and moral decay of consumerism.\n3. The Eyes of T.J. Eckleburg: A silent, fading deity watching commercial greed.\n\nCelebrated quote:\n'So we beat on, boats against the current, borne back ceaselessly into the past.'",
                    isPublic = true,
                    authorHandle = "lit_legend_17"
                ))

                repository.insertNote(Note(
                    title = "Quantum Superposition Intro",
                    subject = "Physics",
                    content = "A quantum system exists in multiple states at once (superposition) until a measurement collapses the wave function!\n\nSchrödinger's Cat is the classic thought experiment.\n- Energy (E) = h * f\n- Planck's constant (h) = 6.626 x 10^-34 J·s\n\nSwitch theme to Dark Academia for deep theoretical vibes.",
                    isPublic = true,
                    authorHandle = "quantum_coder"
                ))

                // Pre-add starter friends
                repository.addFriend("calculus_wiz")
                repository.addFriend("chem_girl_09")
                repository.addFriend("lit_legend_17")

                // Starter disappearing chats
                repository.sendMessage("calculus_wiz", "vibe_pioneer", "Welcome to Vybe! Tap on this note to read it. It will disappear when we close this screen or wait!")
                repository.sendMessage("chem_girl_09", "vibe_pioneer", "midterms are coming up! Let's collaborate and swap notes.")
            }
        }
    }

    // Onboarding Actions
    fun completeOnboarding(handle: String, chosenVibe: VibePreset) {
        viewModelScope.launch {
            val cleanHandle = handle.trim().removePrefix("@")
            val presetConfig = VybeThemeConfig.getPresetConfig(chosenVibe)
            
            val updatedProfile = UserProfile(
                id = 1,
                handle = if (cleanHandle.isEmpty()) "vibe_user" else cleanHandle,
                onboardingComplete = true,
                vibePreset = chosenVibe.name,
                fontStyle = presetConfig.fontStyle.name,
                textSize = presetConfig.textSize.name,
                accentColorHex = presetConfig.accentColorHex,
                cardStyle = presetConfig.cardStyle.name,
                themeMode = presetConfig.themeMode.name,
                backgroundPresetId = presetConfig.backgroundPresetId
            )
            repository.saveUserProfile(updatedProfile)
        }
    }

    fun updateBackgroundPreset(presetId: Int) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                backgroundPresetId = presetId,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    // Customizer Personalization Actions
    fun updateVibePreset(preset: VibePreset) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val presetConfig = VybeThemeConfig.getPresetConfig(preset)
            
            val updated = profile.copy(
                vibePreset = preset.name,
                fontStyle = presetConfig.fontStyle.name,
                textSize = presetConfig.textSize.name,
                accentColorHex = presetConfig.accentColorHex,
                cardStyle = presetConfig.cardStyle.name,
                themeMode = presetConfig.themeMode.name,
                backgroundPresetId = presetConfig.backgroundPresetId
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateFontStyle(fontStyle: FontStylePreset) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                fontStyle = fontStyle.name,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateTextSize(textSize: TextSizePreset) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                textSize = textSize.name,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateAccentColor(hexColor: String) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                accentColorHex = hexColor,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateCardStyle(cardStyle: CardStylePreset) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                cardStyle = cardStyle.name,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateThemeMode(themeMode: ThemeModePreset) {
        viewModelScope.launch {
            val profile = repository.getUserProfile() ?: return@launch
            val updated = profile.copy(
                themeMode = themeMode.name,
                vibePreset = "CUSTOM"
            )
            repository.saveUserProfile(updated)
        }
    }

    // Notes Actions
    fun addNote(title: String, subject: String, content: String, isPublic: Boolean, imagePath: String?) {
        viewModelScope.launch {
            val myHandle = userProfile.value?.handle ?: "me"
            val newNote = Note(
                title = title.trim().ifEmpty { "Untitled Study Note" },
                subject = subject.trim().ifEmpty { "General Study" },
                content = content.trim(),
                imagePath = imagePath,
                isPublic = isPublic,
                authorHandle = myHandle
            )
            repository.insertNote(newNote)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    // Friends Actions
    fun addFriend(handle: String) {
        viewModelScope.launch {
            val cleanHandle = handle.trim().removePrefix("@")
            if (cleanHandle.isNotEmpty() && cleanHandle != userProfile.value?.handle) {
                repository.addFriend(cleanHandle)
            }
        }
    }

    fun removeFriend(handle: String) {
        viewModelScope.launch {
            repository.removeFriend(handle)
        }
    }

    // Direct Messages Actions
    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            val myHandle = userProfile.value?.handle ?: "me"
            val receiver = activeChatFriend.value ?: return@launch
            if (text.trim().isNotEmpty()) {
                repository.sendMessage(myHandle, receiver, text.trim())
            }
        }
    }

    fun markMessageAsViewed(msgId: Int) {
        viewModelScope.launch {
            repository.markMessageAsViewed(msgId)
        }
    }

    fun markMessageAsDisappeared(msgId: Int) {
        viewModelScope.launch {
            repository.markMessageAsDisappeared(msgId)
        }
    }

    fun clearDisappearedMessagesOfActiveFriend() {
        viewModelScope.launch {
            val myHandle = userProfile.value?.handle ?: return@launch
            val friendHandle = activeChatFriend.value ?: return@launch
            val messages = repository.getUndisappearedMessages(myHandle, friendHandle)
            messages.forEach { msg ->
                if (msg.viewed) {
                    repository.markMessageAsDisappeared(msg.id)
                }
            }
        }
    }

    companion object {
        fun provideFactory(repository: VybeRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VybeViewModel(repository) as T
            }
        }
    }
}
