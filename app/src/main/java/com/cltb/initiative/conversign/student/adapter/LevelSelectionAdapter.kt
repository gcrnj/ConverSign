package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.databinding.StudentSelectionInvertedLayoutBinding
import com.cltb.initiative.conversign.databinding.StudentSelectionLayoutBinding

class LevelSelectionAdapter(
    private val selectionList: List<Level>,
    private val onClickListener: (Level) -> Unit
) :
    RecyclerView.Adapter<LevelSelectionAdapter.BaseSelectionViewHolder>() {

    private enum class ViewType {
        NORMAL,
        INVERTED;

        companion object {
            fun fromPosition(position: Int): ViewType = ViewType.entries[position]
        }

        fun position(): Int = entries.toList().indexOf(this)
    }

    abstract inner class BaseSelectionViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        abstract val titleTextView: TextView
        abstract val root: View
        abstract val iconImageView: ImageView

        fun bind(level: Level) {
            titleTextView.text = level.name
            iconImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    root.context,
                    level.icon,
                ),
            )
            root.setOnClickListener {
                onClickListener.invoke(level)
            }
        }
    }

    inner class SelectionViewHolder(private val binding: StudentSelectionLayoutBinding) :
        BaseSelectionViewHolder(binding.root) {

        override val titleTextView: TextView
            get() = binding.titleTextView

        override val root: View
            get() = binding.root

        override val iconImageView: ImageView
            get() = binding.iconImageView
    }

    inner class SelectionInvertedViewHolder(private val binding: StudentSelectionInvertedLayoutBinding) :
        BaseSelectionViewHolder(binding.root) {

        override val titleTextView: TextView
            get() = binding.titleTextView

        override val root: View
            get() = binding.root

        override val iconImageView: ImageView
            get() = binding.iconImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSelectionViewHolder {
        return when (ViewType.fromPosition(viewType)) {
            ViewType.NORMAL -> {
                SelectionViewHolder(
                    StudentSelectionLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ViewType.INVERTED -> {
                SelectionInvertedViewHolder(
                    StudentSelectionInvertedLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position % 2) {
            0 -> ViewType.NORMAL.position()
            else -> ViewType.INVERTED.position()
        }
    }

    override fun getItemCount(): Int {
        return selectionList.size
    }

    override fun onBindViewHolder(holder: BaseSelectionViewHolder, position: Int) {
        holder.bind(selectionList[position])
    }
}