package com.horizontes.puzzle

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class PuzzleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LEVEL_ID = "level_id"
        const val EXTRA_GRID_SIZE = "grid_size"
    }

    private lateinit var engine: PuzzleEngine
    private lateinit var boardView: PuzzleBoardView
    private lateinit var statsText: TextView
    private lateinit var statsStore: PuzzleStatsStore

    private var levelId = 1
    private var gridSize = 3
    private var level: PuzzleLevel? = null
    private var fullBitmap: android.graphics.Bitmap? = null
    private var pieceBitmaps: List<android.graphics.Bitmap> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        levelId = intent.getIntExtra(EXTRA_LEVEL_ID, 1)
        gridSize = intent.getIntExtra(EXTRA_GRID_SIZE, 3)

        level = PuzzleCatalog.levels.find { it.id == levelId } ?: PuzzleCatalog.levels.first()
        statsStore = PuzzleStatsStore(this)
        engine = PuzzleEngine(gridSize)

        boardView = findViewById(R.id.puzzleBoard)
        statsText = findViewById(R.id.statsText)
        boardView.setGridSize(gridSize)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(level!!.titleRes)
        toolbar.setNavigationOnClickListener { finish() }

        loadImageAndStart()

        boardView.onMoveListener = { moves ->
            updateStats(moves)
        }
        boardView.onSolvedListener = { moves, timeMs ->
            statsStore.recordWin(levelId, Difficulty.entries.first { it.gridSize == gridSize }, moves, timeMs)
            showWinDialog(moves, timeMs)
        }

        findViewById<MaterialButton>(R.id.shuffleButton).setOnClickListener {
            engine.shuffle()
            boardView.setup(engine, pieceBitmaps, fullBitmap)
            updateStats(0)
        }

        val previewButton = findViewById<MaterialButton>(R.id.previewButton)
        previewButton.setOnClickListener {
            val showing = previewButton.text == getString(R.string.preview)
            boardView.setPreviewVisible(showing)
            previewButton.text = if (showing) getString(R.string.back_to_puzzle) else getString(R.string.preview)
        }
    }

    private fun loadImageAndStart() {
        val resId = level!!.imageRes
        fullBitmap = BitmapFactory.decodeResource(resources, resId)
        pieceBitmaps = PuzzleImageSlicer.slice(fullBitmap!!, gridSize)
        engine.shuffle()
        boardView.setup(engine, pieceBitmaps, fullBitmap)
        updateStats(0)
    }

    private fun updateStats(moves: Int) {
        val time = formatTime(boardView.getElapsedMs())
        statsText.text = getString(R.string.moves, moves) + "  ·  " + getString(R.string.time, time)
    }

    private fun showWinDialog(moves: Int, timeMs: Long) {
        val time = formatTime(timeMs)
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.you_win))
            .setMessage(
                getString(R.string.moves, moves) + "\n" + getString(R.string.time, time)
            )
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                engine.shuffle()
                boardView.setup(engine, pieceBitmaps, fullBitmap)
                findViewById<MaterialButton>(R.id.previewButton).text = getString(R.string.preview)
                updateStats(0)
            }
            .setNegativeButton(getString(R.string.back_levels)) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun formatTime(ms: Long): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return if (min > 0) "%d:%02d".format(min, sec) else "${sec}s"
    }

    override fun onPause() {
        super.onPause()
        boardView.pauseTimer()
    }
}
