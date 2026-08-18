package com.horizontes.puzzle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private var selectedLevelId = 1
    private var selectedDifficulty = Difficulty.EASY
    private val statsStore = PuzzleStatsStore(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.levelsRecycler)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = LevelAdapter(PuzzleCatalog.levels, statsStore) { level ->
            selectedLevelId = level.id
            (recycler.adapter as LevelAdapter).selectedId = level.id
            recycler.adapter?.notifyDataSetChanged()
        }

        val spinner = findViewById<android.widget.Spinner>(R.id.difficultySpinner)
        val labels = Difficulty.entries.map { getString(it.labelRes) }
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDifficulty = Difficulty.entries[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        findViewById<MaterialButton>(R.id.startButton).setOnClickListener {
            val intent = Intent(this, PuzzleActivity::class.java).apply {
                putExtra(PuzzleActivity.EXTRA_LEVEL_ID, selectedLevelId)
                putExtra(PuzzleActivity.EXTRA_GRID_SIZE, selectedDifficulty.gridSize)
            }
            startActivity(intent)
        }
    }
}

private class LevelAdapter(
    private val levels: List<PuzzleLevel>,
    private val statsStore: PuzzleStatsStore,
    var selectedId: Int = 1,
    private val onSelect: (PuzzleLevel) -> Unit
) : RecyclerView.Adapter<LevelAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val thumb: ImageView = view.findViewById(R.id.levelThumbnail)
        val title: TextView = view.findViewById(R.id.levelTitle)
        val best: TextView = view.findViewById(R.id.levelBest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = levels.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val level = levels[position]
        holder.thumb.setImageResource(level.imageRes)
        holder.title.text = holder.itemView.context.getString(level.titleRes)
        holder.best.text = statsStore.getLevelSummary(level.id)
        holder.itemView.alpha = if (level.id == selectedId) 1f else 0.65f
        holder.itemView.setOnClickListener { onSelect(level) }
    }
}
