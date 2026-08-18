package com.horizontes.puzzle

enum class Difficulty(val gridSize: Int, val labelRes: Int) {
    EASY(3, R.string.difficulty_easy),
    MEDIUM(4, R.string.difficulty_medium),
    HARD(5, R.string.difficulty_hard),
    EXPERT(6, R.string.difficulty_expert);

    val pieceCount: Int get() = gridSize * gridSize
}

data class PuzzleLevel(
    val id: Int,
    val imageRes: Int,
    val titleRes: Int
)

object PuzzleCatalog {
    val levels = listOf(
        PuzzleLevel(1, R.drawable.puzzle_photo_1, R.string.level_1_title),
        PuzzleLevel(2, R.drawable.puzzle_photo_2, R.string.level_2_title),
        PuzzleLevel(3, R.drawable.puzzle_photo_3, R.string.level_3_title),
        PuzzleLevel(4, R.drawable.puzzle_photo_4, R.string.level_4_title),
        PuzzleLevel(5, R.drawable.puzzle_photo_5, R.string.level_5_title)
    )
}
