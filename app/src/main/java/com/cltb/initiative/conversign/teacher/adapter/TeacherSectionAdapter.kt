package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.ItemSectionBinding

class TeacherSectionAdapter(
    private val sections: List<Section>,
    private val onLevelSelected: (Pair<Section, Level>) -> Unit,
) : RecyclerView.Adapter<TeacherSectionAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(val binding: ItemSectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.binding.sectionTitle.text = section.name

        // Expand/collapse levels
        val levelAdapter = TeacherLevelAdapter(section.levels) { level ->
            onLevelSelected.invoke(Pair(section, level))
        }
        holder.binding.levelsRecyclerView.adapter = levelAdapter
        holder.binding.levelsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
//        holder.binding.levelsRecyclerView.visibility = if (section.isExpanded) View.VISIBLE else View.GONE

        holder.binding.sectionTitle.setOnClickListener {
//            section.isExpanded = !section.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = sections.size
}
