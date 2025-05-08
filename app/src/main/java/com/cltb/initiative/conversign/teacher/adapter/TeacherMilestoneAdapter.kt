package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.databinding.ItemMilestoneBinding

class TeacherMilestoneAdapter(
    private val milestones: List<Milestone>
) : RecyclerView.Adapter<TeacherMilestoneAdapter.MilestoneViewHolder>() {

    inner class MilestoneViewHolder(val binding: ItemMilestoneBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding = ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.binding.milestoneTitle.text = milestones[position].roadMapTitle
    }

    override fun getItemCount() = milestones.size
}
