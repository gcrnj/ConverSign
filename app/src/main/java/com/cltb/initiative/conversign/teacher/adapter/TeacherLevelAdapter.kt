package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.databinding.ItemLevelBinding

class TeacherLevelAdapter(
    private val levels: List<Level>,
    private val onLevelSelected: (Level) -> Unit,
) : RecyclerView.Adapter<TeacherLevelAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) = with(holder.binding) {
        val level = levels[position]
        levelTitle.text = level.name

        // Expand/collapse milestones
        val milestoneAdapter = TeacherMilestoneAdapter(level.milestones)
//        milestonesRecyclerView.adapter = milestoneAdapter
        milestonesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)

        levelTitle.setOnClickListener {
            notifyItemChanged(position)
        }
        root.setOnClickListener {
            onLevelSelected.invoke(level)
        }
        levelTitle.setOnClickListener {
            onLevelSelected.invoke(level)
        }
    }

    override fun getItemCount() = levels.size
}
