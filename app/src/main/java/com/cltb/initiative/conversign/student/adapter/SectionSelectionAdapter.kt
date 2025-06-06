package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.StudentSectionSelectionLayoutBinding

class SectionSelectionAdapter(
    private val sectionList: List<Section>,
    private val progress: Progress,
    private val onClick: (Section) -> Unit
) : RecyclerView.Adapter<SectionSelectionAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(private val binding: StudentSectionSelectionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: Section) = with(binding) {
            sectionTitleTextView.text = section.name
            root.setOnClickListener {
                if (progress.section >= section.sectionNumber) {
                    onClick(section)
                }
            }

            // Enable / Disable
            if (progress.section >= section.sectionNumber) {
                root.alpha = 1f
            } else {
                root.alpha = 0.5f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = StudentSectionSelectionLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SectionViewHolder(binding)
    }

    override fun getItemCount(): Int = sectionList.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(sectionList[position])
    }
}
