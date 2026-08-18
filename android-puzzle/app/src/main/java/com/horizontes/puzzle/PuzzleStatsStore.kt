package com.horizontes.puzzle

import android.content.Context
import android.content.SharedPreferences

class PuzzleStatsStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("horizontes_puzzle_stats", Context.MODE_PRIVATE)

    fun getBestMoves(levelId: Int, difficulty: Difficulty): Int? {
        val key = key(levelId, difficulty, "moves")
        return if (prefs.contains(key)) prefs.getInt(key, Int.MAX_VALUE) else null
    }

    fun getBestTimeMs(levelId: Int, difficulty: Difficulty): Long? {
        val key = key(levelId, difficulty, "time")
        return if (prefs.contains(key)) prefs.getLong(key, Long.MAX_VALUE) else null
    }

    fun recordWin(levelId: Int, difficulty: Difficulty, moves: Int, timeMs: Long) {
        val movesKey = key(levelId, difficulty, "moves")
        val timeKey = key(levelId, difficulty, "time")
        val prevMoves = prefs.getInt(movesKey, Int.MAX_VALUE)
        val prevTime = prefs.getLong(timeKey, Long.MAX_VALUE)
        if (moves < prevMoves) prefs.edit().putInt(movesKey, moves).apply()
        if (timeMs < prevTime) prefs.edit().putLong(timeKey, timeMs).apply()
    }

    fun getLevelSummary(levelId: Int): String {
        val bestParts = Difficulty.entries.mapNotNull { diff ->
            val moves = getBestMoves(levelId, diff)
            if (moves != null) "${diff.gridSize}×${diff.gridSize}: $moves mov." else null
        }
        return if (bestParts.isEmpty()) "Sin récord" else bestParts.joinToString(" · ")
    }

    private fun key(levelId: Int, difficulty: Difficulty, suffix: String): String =
        "l${levelId}_g${difficulty.gridSize}_$suffix"
}
