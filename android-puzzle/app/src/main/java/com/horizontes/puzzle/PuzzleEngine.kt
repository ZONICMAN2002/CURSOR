package com.horizontes.puzzle

import android.graphics.Bitmap
import kotlin.random.Random

class PuzzleEngine(private val gridSize: Int) {
    /** Index in the shuffled board; value is the correct piece id (row*grid + col). */
    val board: IntArray = IntArray(gridSize * gridSize) { it }

    var moveCount: Int = 0
    var isSolved: Boolean = false

    fun shuffle(moves: Int = gridSize * gridSize * 8) {
        moveCount = 0
        isSolved = false
        for (i in board.indices) board[i] = i

        var lastFrom = -1
        var lastTo = -1
        repeat(moves) {
            val options = mutableListOf<Pair<Int, Int>>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (i != j && (i != lastFrom && j != lastTo)) {
                        options.add(i to j)
                    }
                }
            }
            if (options.isEmpty()) return@repeat
            val (from, to) = options[Random.nextInt(options.size)]
            swap(from, to, countMove = false)
            lastFrom = from
            lastTo = to
        }
        if (isComplete()) {
            shuffle(moves + 4)
        }
        moveCount = 0
        isSolved = false
    }

    fun swap(fromIndex: Int, toIndex: Int, countMove: Boolean = true): Boolean {
        if (fromIndex == toIndex) return false
        if (fromIndex !in board.indices || toIndex !in board.indices) return false
        val temp = board[fromIndex]
        board[fromIndex] = board[toIndex]
        board[toIndex] = temp
        if (countMove) {
            moveCount++
            isSolved = isComplete()
        }
        return true
    }

    fun isComplete(): Boolean = board.indices.all { board[it] == it }

    fun pieceAt(cellIndex: Int): Int = board[cellIndex]

    fun correctRow(pieceId: Int): Int = pieceId / gridSize
    fun correctCol(pieceId: Int): Int = pieceId % gridSize
}

object PuzzleImageSlicer {
    fun slice(bitmap: Bitmap, gridSize: Int): List<Bitmap> {
        val pieces = mutableListOf<Bitmap>()
        val w = bitmap.width
        val h = bitmap.height
        val cellW = w / gridSize
        val cellH = h / gridSize
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val piece = Bitmap.createBitmap(
                    bitmap,
                    col * cellW,
                    row * cellH,
                    cellW,
                    cellH
                )
                pieces.add(piece)
            }
        }
        return pieces
    }
}
