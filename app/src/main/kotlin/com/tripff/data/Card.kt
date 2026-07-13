package com.tripff.data

enum class Player { BLUE, RED }

data class Card(
    val id: Int,
    val name: String,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val left: Int,
    val level: Int,
    val symbol: String
) {
    private fun valueStr(v: Int): String = if (v == 10) "A" else v.toString()

    val topStr: String get() = valueStr(top)
    val rightStr: String get() = valueStr(right)
    val bottomStr: String get() = valueStr(bottom)
    val leftStr: String get() = valueStr(left)

    fun totalPower(): Int = top + right + bottom + left
}

data class BoardCard(
    val card: Card,
    val owner: Player
)
