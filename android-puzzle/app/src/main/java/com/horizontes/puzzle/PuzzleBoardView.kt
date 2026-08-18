package com.horizontes.puzzle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class PuzzleBoardView(context: Context) : View(context) {
    private var gridSize = 3
    private var engine: PuzzleEngine? = null
    private var pieceBitmaps: List<Bitmap> = emptyList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = ContextCompat.getColor(context, R.color.horizontes_blue_light)
    }
    private val correctTint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.success_green)
        alpha = 60
    }

    private var cellSize = 0f
    private var boardLeft = 0f
    private var boardTop = 0f
    private var draggingCell = -1
    private var dragX = 0f
    private var dragY = 0f
    private var showPreview = false
    private var fullBitmap: Bitmap? = null

    var onSolvedListener: ((Int, Long) -> Unit)? = null
    var onMoveListener: ((Int) -> Unit)? = null

    private var elapsedMs: Long = 0
    private var runningSince: Long = 0
    private var isTimerRunning = false

    fun setup(
        engine: PuzzleEngine,
        pieces: List<Bitmap>,
        fullImage: Bitmap?
    ) {
        this.engine = engine
        this.pieceBitmaps = pieces
        this.fullBitmap = fullImage
        draggingCell = -1
        elapsedMs = 0
        runningSince = System.currentTimeMillis()
        isTimerRunning = true
        invalidate()
    }

    fun setPreviewVisible(visible: Boolean) {
        showPreview = visible
        invalidate()
    }

    fun getElapsedMs(): Long {
        return if (isTimerRunning) {
            elapsedMs + (System.currentTimeMillis() - runningSince)
        } else {
            elapsedMs
        }
    }

    fun pauseTimer() {
        if (isTimerRunning) {
            elapsedMs += System.currentTimeMillis() - runningSince
            isTimerRunning = false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val side = minOf(w, h).toFloat()
        cellSize = side / gridSize
        boardLeft = (w - side) / 2f
        boardTop = (h - side) / 2f
    }

    fun setGridSize(size: Int) {
        gridSize = size
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val eng = engine ?: return
        if (pieceBitmaps.isEmpty()) return

        if (showPreview && fullBitmap != null) {
            val side = cellSize * gridSize
            val dest = Rect(
                boardLeft.toInt(),
                boardTop.toInt(),
                (boardLeft + side).toInt(),
                (boardTop + side).toInt()
            )
            canvas.drawBitmap(fullBitmap!!, null, dest, paint)
            canvas.drawRect(dest, borderPaint)
            return
        }

        for (cell in eng.board.indices) {
            if (cell == draggingCell) continue
            drawCell(canvas, eng, cell)
        }

        if (draggingCell >= 0) {
            val pieceId = eng.pieceAt(draggingCell)
            val bmp = pieceBitmaps.getOrNull(pieceId)
            if (bmp != null) {
                val left = dragX - cellSize / 2
                val top = dragY - cellSize / 2
                canvas.drawBitmap(bmp, null, Rect(left.toInt(), top.toInt(), (left + cellSize).toInt(), (top + cellSize).toInt()), paint)
                canvas.drawRect(left, top, left + cellSize, top + cellSize, borderPaint)
            }
        }
    }

    private fun drawCell(canvas: Canvas, eng: PuzzleEngine, cellIndex: Int) {
        val row = cellIndex / gridSize
        val col = cellIndex % gridSize
        val left = boardLeft + col * cellSize
        val top = boardTop + row * cellSize
        val pieceId = eng.pieceAt(cellIndex)
        val bmp = pieceBitmaps.getOrNull(pieceId)
        if (bmp != null) {
            canvas.drawBitmap(bmp, null, Rect(left.toInt(), top.toInt(), (left + cellSize).toInt(), (top + cellSize).toInt()), paint)
        }
        if (pieceId == cellIndex) {
            canvas.drawRect(left, top, left + cellSize, top + cellSize, correctTint)
        }
        canvas.drawRect(left, top, left + cellSize, top + cellSize, borderPaint)
    }

    private fun cellAt(x: Float, y: Float): Int {
        val col = ((x - boardLeft) / cellSize).toInt()
        val row = ((y - boardTop) / cellSize).toInt()
        if (col !in 0 until gridSize || row !in 0 until gridSize) return -1
        return row * gridSize + col
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eng = engine ?: return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val cell = cellAt(event.x, event.y)
                if (cell >= 0) {
                    draggingCell = cell
                    dragX = event.x
                    dragY = event.y
                    parent.requestDisallowInterceptTouchEvent(true)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (draggingCell >= 0) {
                    dragX = event.x
                    dragY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (draggingCell >= 0) {
                    val target = cellAt(event.x, event.y)
                    if (target >= 0 && target != draggingCell) {
                        eng.swap(draggingCell, target)
                        onMoveListener?.invoke(eng.moveCount)
                        if (eng.isSolved) {
                            pauseTimer()
                            onSolvedListener?.invoke(eng.moveCount, getElapsedMs())
                        }
                    }
                    draggingCell = -1
                    invalidate()
                }
            }
        }
        return true
    }
}
