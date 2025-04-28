package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.SelectionData
import com.cltb.initiative.conversign.databinding.StudentSelectionLayoutBinding

class SelectionAdapter(private val selectionList: List<SelectionData>, private val onClickListener: (SelectionData) -> Unit) :
    RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder>() {


    inner class SelectionViewHolder(val binding: StudentSelectionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(selectionData: SelectionData) = with(binding) {
            titleTextView.text =
                binding.root.context.getString(
                    R.string.selection_title,
                    selectionData.type,
                    selectionData.id.toString(),
                    selectionData.name,
                )
            root.setOnClickListener {
                onClickListener.invoke(selectionData)
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