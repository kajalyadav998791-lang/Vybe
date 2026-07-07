package com.example.ui.theme

import androidx.compose.ui.graphics.Color

data class BackgroundPreset(
    val id: Int,
    val name: String,
    val category: String,
    val isDark: Boolean,
    val colors: List<Color>,
    val patternType: String, // "none", "grid", "dots", "stars", "lines"
    val imageUrl: String? = null
)

object BackgroundStylePresets {
    val presets: List<BackgroundPreset> = run {
        val list = mutableListOf<BackgroundPreset>()

        // 1. Instagram Dusk & Aurora Gradients (1 to 20)
        val instaPalettes = listOf(
            listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFF56040), Color(0xFFFCAF45)), // Classic Insta
            listOf(Color(0xFF2C3E50), Color(0xFF3498DB), Color(0xFF2980B9)), // Blue Lagoon
            listOf(Color(0xFFFF007F), Color(0xFF7F00FF)), // Neon Pink-Purple
            listOf(Color(0xFF00FFCC), Color(0xFF0099FF)), // Cyber Cyan-Blue
            listOf(Color(0xFFFF416C), Color(0xFFFF4B2B)), // Burning Sun
            listOf(Color(0xFF000000), Color(0xFF130F40)), // Deep Space
            listOf(Color(0xFFFA8BFF), Color(0xFF2BD2FF), Color(0xFF2BFF88)), // Sweet Dream
            listOf(Color(0xFF4158D0), Color(0xFFC850C0), Color(0xFFFFCC70)), // Dusk Glow
            listOf(Color(0xFF13131A), Color(0xFF211134), Color(0xFF0B1F3D)), // Cosmic Aurora
            listOf(Color(0xFFD4145A), Color(0xFFFBB03B)), // Golden Hour
            listOf(Color(0xFF662D8C), Color(0xFFED1E79)), // Velvet Dusk
            listOf(Color(0xFF11998E), Color(0xFF38EF7D)), // Emerald City
            listOf(Color(0xFF00C6FF), Color(0xFF0072FF)), // Pacific Crest
            listOf(Color(0xFF7028E4), Color(0xFFE20D18)), // Royal Red-Violet
            listOf(Color(0xFF051730), Color(0xFF0C2C5E)), // Oceanic Midnight
            listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF), Color(0xFFFECFEF)), // Soft Sunset
            listOf(Color(0xFF2193B0), Color(0xFF6DD5ED)), // Sky Blue
            listOf(Color(0xFFEE0979), Color(0xFFFF6A00)), // Flame Neon
            listOf(Color(0xFF1F1C2C), Color(0xFF928DAB)), // Dark Ash-Silver
            listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)) // Metal Slate
        )
        instaPalettes.forEachIndexed { i, colors ->
            list.add(
                BackgroundPreset(
                    id = i + 1,
                    name = when(i) {
                        0 -> "Classic Sunset"
                        1 -> "Blue Lagoon"
                        2 -> "Magenta Magic"
                        3 -> "Cyber Wave"
                        4 -> "Solar Flare"
                        5 -> "Deep Indigo"
                        6 -> "Vibrant Aurora"
                        7 -> "Dusk Symphony"
                        8 -> "Stellar Night"
                        9 -> "Golden hour"
                        10 -> "Velvet Rose"
                        11 -> "Minty Fresh"
                        12 -> "Pacific Tide"
                        13 -> "Crimson Violet"
                        14 -> "Deep Abyss"
                        15 -> "Pink Dream"
                        16 -> "Ethereal Sky"
                        17 -> "Electric Orange"
                        18 -> "Gunmetal Grey"
                        else -> "Slate Matrix"
                    },
                    category = "Instagram Gradients",
                    isDark = i in listOf(0, 1, 4, 5, 8, 10, 13, 14, 18, 19),
                    colors = colors,
                    patternType = if (i % 3 == 0) "lines" else "none"
                )
            )
        }

        // 2. Pinterest Pastel & Soft Cores (21 to 40)
        val pastelColors = listOf(
            listOf(Color(0xFFFBF4FF), Color(0xFFF0E5FF)), // Lavender Blush
            listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)), // Minty Fresh
            listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)), // Peach Mousse
            listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC)), // Soft Pearl
            listOf(Color(0xFFFFFDE7), Color(0xFFFFF9C4)), // Lemon Cream
            listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC)), // Cotton Candy
            listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0)), // Fairy Dust
            listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2)), // Aqua Mist
            listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)), // Soft Orchid
            listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8)), // Warm Oatmeal
            listOf(Color(0xFFF9FBE7), Color(0xFFF0F4C3)), // Matcha Foam
            listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8)), // Sage Sprig
            listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2)), // Rosy Cheeks
            listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9)), // Powder Blue
            listOf(Color(0xFFF0F4C3), Color(0xFFE8F5E9)), // Matcha Latte
            listOf(Color(0xFFFFF3E0), Color(0xFFFCE4EC)), // Peach Sherbet
            listOf(Color(0xFFE0F2F1), Color(0xFFB2DFDB)), // Pale Jade
            listOf(Color(0xFFFFF9C4), Color(0xFFFFE0B2)), // Butterscotch
            listOf(Color(0xFFECEFF1), Color(0xFFF3E5F5)), // Slate Lavender
            listOf(Color(0xFFFFF0F5), Color(0xFFE6E6FA))  // Lavender Rose
        )
        pastelColors.forEachIndexed { i, colors ->
            list.add(
                BackgroundPreset(
                    id = 21 + i,
                    name = when(i) {
                        0 -> "Lavender Cream"
                        1 -> "Sweet Mint"
                        2 -> "Warm Apricot"
                        3 -> "Snow Pearl"
                        4 -> "Custard Pie"
                        5 -> "Sky Cloud"
                        6 -> "Cherry Blossom"
                        7 -> "Ocean Spray"
                        8 -> "Lilac Dew"
                        9 -> "Pecan Beige"
                        10 -> "Matcha Tea"
                        11 -> "Pistachio"
                        12 -> "Blush Rose"
                        13 -> "Chalky Indigo"
                        14 -> "Tea Latte"
                        15 -> "Sherbet Blend"
                        16 -> "Celadon Mint"
                        17 -> "Golden Butter"
                        18 -> "Lilac Grey"
                        else -> "Orchid Mist"
                    },
                    category = "Pinterest Softcores",
                    isDark = false,
                    colors = colors,
                    patternType = "dots"
                )
            )
        }

        // 3. Minimalist Tokyo-style Monochromes (41 to 55)
        val monoColors = listOf(
            listOf(Color(0xFF121212), Color(0xFF1C1C1C)), // Stark Black
            listOf(Color(0xFF1E1E24), Color(0xFF2D2D34)), // Matte Charcoal
            listOf(Color(0xFFFAFAFA), Color(0xFFF0F0F0)), // Pure White
            listOf(Color(0xFFECECEC), Color(0xFFDBDBDB)), // Concrete Cement
            listOf(Color(0xFF2C3E50), Color(0xFF1E272C)), // Prussian Grey
            listOf(Color(0xFF0F172A), Color(0xFF1E293B)), // Obsidian Ink
            listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)), // Clean Slate
            listOf(Color(0xFF090D16), Color(0xFF111827)), // Midnight Void
            listOf(Color(0xFF1F2937), Color(0xFF111827)), // Shadow Steel
            listOf(Color(0xFFD1D5DB), Color(0xFF9CA3AF)), // Silver Ash
            listOf(Color(0xFF4B5563), Color(0xFF374151)), // Iron Metal
            listOf(Color(0xFF000000), Color(0xFF080808)), // Absolute Void
            listOf(Color(0xFFF3F4F6), Color(0xFFE5E7EB)), // Soft Chalk
            listOf(Color(0xFFE5E7EB), Color(0xFFD1D5DB)), // Light Concrete
            listOf(Color(0xFF1F2937), Color(0xFF030712))  // Pitch Black
        )
        monoColors.forEachIndexed { i, colors ->
            list.add(
                BackgroundPreset(
                    id = 41 + i,
                    name = when(i) {
                        0 -> "Tokyo Cement"
                        1 -> "Matte Charcoal"
                        2 -> "Minimalist White"
                        3 -> "Concrete Block"
                        4 -> "Slate Stone"
                        5 -> "Obsidian Ink"
                        6 -> "Clean Steel"
                        7 -> "Midnight Jet"
                        8 -> "Shadow Ore"
                        9 -> "Aesthetic Silver"
                        10 -> "Iron Plate"
                        11 -> "Onyx Black"
                        12 -> "Soft Paper"
                        13 -> "Light Granite"
                        else -> "Graphite Shimmer"
                    },
                    category = "Tokyo Monochromes",
                    isDark = i != 2 && i != 3 && i != 6 && i != 9 && i != 12 && i != 13,
                    colors = colors,
                    patternType = "grid"
                )
            )
        }

        // 4. Dark Cyberpunk & Cyber Glow Grid/Scanline Textures (56 to 70)
        val cyberColors = listOf(
            listOf(Color(0xFF03001E), Color(0xFF7303C0), Color(0xFFEC38BC)), // Neon Cyber
            listOf(Color(0xFF000000), Color(0xFF00FFCC)), // Cyan Grid
            listOf(Color(0xFF05050A), Color(0xFF140F2D)), // Acid Violet
            listOf(Color(0xFF080018), Color(0xFF26004C)), // Midnight Matrix
            listOf(Color(0xFF0F0014), Color(0xFFFF0055)), // Hotline Rose
            listOf(Color(0xFF000B18), Color(0xFF00E5FF)), // Hyperion Cyan
            listOf(Color(0xFF120024), Color(0xFF4A00E0)), // Ultraviolet
            listOf(Color(0xFF030303), Color(0xFFC1F80A)), // Acid Lime
            listOf(Color(0xFF1C1124), Color(0xFF0F0816)), // Neon Shadow
            listOf(Color(0xFF0F2027), Color(0xFF203A43)), // Dark Tech
            listOf(Color(0xFF000000), Color(0xFFFF007F)), // Pink Grid
            listOf(Color(0xFF05110E), Color(0xFF11322C)), // Forest Digital
            listOf(Color(0xFF110B29), Color(0xFF1F1147)), // Digital Sky
            listOf(Color(0xFF1A0033), Color(0xFF00FF99)), // Cyber Green
            listOf(Color(0xFF0D0221), Color(0xFF2A085C))  // Dark Synth
        )
        cyberColors.forEachIndexed { i, colors ->
            list.add(
                BackgroundPreset(
                    id = 56 + i,
                    name = when(i) {
                        0 -> "Synthwave Dream"
                        1 -> "Cyan Gridline"
                        2 -> "Acid Violet"
                        3 -> "Matrix Net"
                        4 -> "Hotline Rose"
                        5 -> "Hyperion Light"
                        6 -> "Ultraviolet Glow"
                        7 -> "Acid Lime Cyber"
                        8 -> "Violet Darknet"
                        9 -> "Military Tech"
                        10 -> "Magma Core"
                        11 -> "Matrix Green"
                        12 -> "Nebula Cyber"
                        13 -> "Digital Jade"
                        else -> "Retro Synth"
                    },
                    category = "Cyberpunk Textures",
                    isDark = true,
                    colors = colors,
                    patternType = "grid"
                )
            )
        }

        // 5. Dark Academia Leather, Mahogany & Parchment Tones (71 to 85)
        val academiaColors = listOf(
            listOf(Color(0xFF1C1410), Color(0xFF2C1E18)), // Antique Leather
            listOf(Color(0xFF241C15), Color(0xFF382A20)), // Roasted Espresso
            listOf(Color(0xFF1B241C), Color(0xFF2A382A)), // Library Ivy Green
            listOf(Color(0xFFFAF3E0), Color(0xFFE6D7B8)), // Vintage Parchment
            listOf(Color(0xFF181B24), Color(0xFF282C38)), // Midnight Scholar
            listOf(Color(0xFF2E1A1A), Color(0xFF402222)), // Burgundy Velvet
            listOf(Color(0xFF111115), Color(0xFF22222B)), // Dark Charcoal
            listOf(Color(0xFFF4ECD8), Color(0xFFE4D5B1)), // Aged Book Page
            listOf(Color(0xFF231C1A), Color(0xFF352926)), // Dark Walnut
            listOf(Color(0xFF1A221E), Color(0xFF29352F)), // Forest Lodge
            listOf(Color(0xFFF9F6F0), Color(0xFFEFE9DC)), // Ivory Linen
            listOf(Color(0xFF16110E), Color(0xFF29201A)), // Dark academia
            listOf(Color(0xFF3E2723), Color(0xFF4E342E)), // Rich Mahogany
            listOf(Color(0xFF263238), Color(0xFF37474F)), // Slate Scholar
            listOf(Color(0xFFF5F5DC), Color(0xFFE5D5B8))  // Antique Beige
        )
        academiaColors.forEachIndexed { i, colors ->
            list.add(
                BackgroundPreset(
                    id = 71 + i,
                    name = when(i) {
                        0 -> "Antique Leather"
                        1 -> "Roasted Espresso"
                        2 -> "Library Ivy"
                        3 -> "Vintage Parchment"
                        4 -> "Midnight Scholar"
                        5 -> "Burgundy Wax"
                        6 -> "Gothic Stone"
                        7 -> "Aged Book"
                        8 -> "Dark Walnut"
                        9 -> "Forest Lodge"
                        10 -> "Ivory Linen"
                        11 -> "Deep Academia"
                        12 -> "Rich Mahogany"
                        13 -> "Slate Library"
                        else -> "Antique Beige"
                    },
                    category = "Dark Academia",
                    isDark = i != 3 && i != 7 && i != 10 && i != 14,
                    colors = colors,
                    patternType = "lines"
                )
            )
        }

        // 6. Aesthetic Unsplash Mood Wallpapers (86 to 100)
        // High quality stable aesthetic Unsplash imagery matching different genres
        val unsplashUrls = listOf(
            "https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?w=500&auto=format&fit=crop" to "Flora Abstract",
            "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=500&auto=format&fit=crop" to "Liquid Acrylic",
            "https://images.unsplash.com/photo-1518531933037-91b2f5f229cc?w=500&auto=format&fit=crop" to "Dreamy Bokeh",
            "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop" to "Cosmic Nebula",
            "https://images.unsplash.com/photo-1506318137071-a8e063b4bec0?w=500&auto=format&fit=crop" to "Silent Starry Night",
            "https://images.unsplash.com/photo-1528459801416-a9e53bbf4e17?w=500&auto=format&fit=crop" to "Creamy Marble",
            "https://images.unsplash.com/photo-1464802686167-b939a6910659?w=500&auto=format&fit=crop" to "Deep Galaxy",
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500&auto=format&fit=crop" to "Summertime Sand",
            "https://images.unsplash.com/photo-1519751138087-5bf79df62d5b?w=500&auto=format&fit=crop" to "Aesthetic Pink Petals",
            "https://images.unsplash.com/photo-1475924156734-496f6cac6ec1?w=500&auto=format&fit=crop" to "Aesthetic Sunrise",
            "https://images.unsplash.com/photo-1524169358666-79f22534bc6e?w=500&auto=format&fit=crop" to "Anime Sky Sunset",
            "https://images.unsplash.com/photo-1518156677180-95a2893f3e9f?w=500&auto=format&fit=crop" to "Cyber City Rain",
            "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=500&auto=format&fit=crop" to "Forest Whisper",
            "https://images.unsplash.com/photo-1557683316-973673baf926?w=500&auto=format&fit=crop" to "Warm Grainy Film",
            "https://images.unsplash.com/photo-1516339901601-2e1d62dc0c45?w=500&auto=format&fit=crop" to "Space Nebula Glow"
        )
        unsplashUrls.forEachIndexed { i, (url, name) ->
            list.add(
                BackgroundPreset(
                    id = 86 + i,
                    name = name,
                    category = "Aesthetic Moods",
                    isDark = i in listOf(1, 3, 4, 6, 11, 12, 14),
                    colors = listOf(Color(0xFF1E1E24), Color(0xFF0F0F14)),
                    patternType = "none",
                    imageUrl = url
                )
            )
        }

        list
    }
}
