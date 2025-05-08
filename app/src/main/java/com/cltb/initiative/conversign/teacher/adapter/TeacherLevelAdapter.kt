package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.databinding.ItemLevelBinding

class TeacherLevelAdapter(
    private val levels: List<Level>
) : RecyclerView.Adapter<TeacherLevelAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levels[position]
        holder.binding.levelTitle.text = level.name

        // Expand/collapse milestones
        val milestoneAdapter = TeacherMilestoneAdapter(level.milestones)
        holder.binding.milestonesRecyclerView.adapter = milestoneAdapter
        holder.binding.milestonesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
//        holder.binding.milestonesRecyclerView.visibility = if (true) View.VISIBLE else View.GONE

        holder.binding.levelTitle.setOnClickListener {
//            level.isExpanded = !level.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = levels.size
}
