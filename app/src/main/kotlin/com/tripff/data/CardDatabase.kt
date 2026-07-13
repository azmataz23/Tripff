package com.tripff.data

import kotlin.random.Random

enum class Difficulty(val label: String, val minLevel: Int, val maxLevel: Int) {
    EASY("Easy", 1, 2),
    MEDIUM("Medium", 2, 4),
    HARD("Hard", 3, 5)
}

object CardDatabase {

    val all = listOf(
        // Level 1 — Weak fodder
        Card(1,  "Bog Crawler",     1, 2, 3, 2, 1, "◈"),
        Card(2,  "Stone Pebble",    2, 1, 2, 1, 1, "▲"),
        Card(3,  "Mud Slime",       1, 3, 2, 1, 1, "●"),
        Card(4,  "Spark Fly",       3, 1, 1, 2, 1, "✦"),
        Card(5,  "Leaf Sprite",     2, 2, 1, 3, 1, "✿"),

        // Level 2 — Common
        Card(6,  "Iron Goat",       3, 4, 2, 3, 2, "◆"),
        Card(7,  "Frost Imp",       4, 2, 3, 3, 2, "❋"),
        Card(8,  "Flame Newt",      2, 3, 4, 4, 2, "✶"),
        Card(9,  "Thunder Rat",     4, 4, 3, 2, 2, "⚡"),
        Card(10, "Shadow Fern",     3, 3, 4, 3, 2, "✵"),

        // Level 3 — Uncommon
        Card(11, "Storm Hawk",      5, 4, 4, 5, 3, "✈"),
        Card(12, "Magma Crab",      4, 6, 5, 4, 3, "✸"),
        Card(13, "Void Sprite",     6, 4, 4, 5, 3, "☽"),
        Card(14, "Crystal Drake",   5, 5, 6, 4, 3, "◇"),
        Card(15, "Dark Wraith",     4, 5, 5, 6, 3, "✞"),

        // Level 4 — Rare
        Card(16, "Solar Lion",      7, 6, 5, 6, 4, "☀"),
        Card(17, "Lunar Wolf",      6, 7, 6, 5, 4, "☾"),
        Card(18, "Prism Eagle",     6, 6, 7, 6, 4, "★"),
        Card(19, "Aether Serpent",  5, 6, 6, 7, 4, "∞"),
        Card(20, "Doom Knight",     7, 7, 5, 5, 4, "⚔"),

        // Level 5 — Legendary
        Card(21, "Celestial Dragon", 9, 8, 6, 7, 5, "♦"),
        Card(22, "Void Sovereign",   8, 9, 7, 6, 5, "◉"),
        Card(23, "Prime Titan",      7, 8, 9, 8, 5, "▣"),
        Card(24, "Astral Guardian",  8, 7, 8, 9, 5, "✪"),
        Card(25, "Eternal Phoenix",  10, 8, 7, 9, 5, "♛")
    )

    fun deckForDifficulty(difficulty: Difficulty, rng: Random = Random): List<Card> =
        all.filter { it.level in difficulty.minLevel..difficulty.maxLevel }
            .shuffled(rng)
            .take(5)

    fun playerStarterDeck(rng: Random = Random): List<Card> =
        all.filter { it.level in 1..3 }.shuffled(rng).take(5)
}
