package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.databinding.StudentSelectionLayoutBinding

class SelectionAdapter(private val selectionList: List<Level>, private val onClickListener: (Level) -> Unit) :
    RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder>() {


    inner class SelectionViewHolder(val binding: StudentSelectionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(level: Level) = with(binding) {
            titleTextView.text = level.name
            root.setOnClickListener {
                onClickListener.invoke(level)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder {
        return SelectionViewHolder(
            StudentSelectionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return selectionList.size
    }

    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        holder.bind(selectionList[position])
    }
}