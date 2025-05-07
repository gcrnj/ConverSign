package com.cltb.initiative.conversign.student.adapter

import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.databinding.RoadMapNodeLayoutBinding

class RoadMapAdapter(
    private val level: Level,
    private val progress: Progress,
    private val selectedMilestoneNumber: Int,
    private val onMilestoneSelected: (Milestone) -> Unit,
) :
    RecyclerView.Adapter<RoadMapAdapter.RoadMapViewHolderV2>() {


    private val milestones = level.milestones

    inner class RoadMapViewHolderV2(private val viewBinding: RoadMapNodeLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

            private fun selectedBackground() = with(viewBinding) {
                textView.setBackgroundResource(R.drawable.roadmap_node_selected)
                textView.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.screen_bg
                    )
                )
            }

        private fun lockedBackground() = with(viewBinding) {
            textView.setBackgroundResource(R.drawable.roadmap_node_locked)
            textView.setTextColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.screen_bg
                )
            )
        }
        fun bind(milestone: Milestone, position: Int) = with(viewBinding) {
            textView.text = milestone.roadMapTitle

            if (milestone.number <= selectedMilestoneNumber) {
                if (milestone.number == selectedMilestoneNumber) {
                    selectedBackground()
                }
                textView.setOnClickListener {
                    onMilestoneSelected.invoke(milestone)
                }
            } else {
                lockedBackground()
            }


            // Post to ensure layout is measured
            root.post {
                val parentWidth = (root.parent as View).width
                val fourth = parentWidth / 4

                val constraintSet = ConstraintSet()
                constraintSet.clone(root) // root is ConstraintLayout

                // Clear old horizontal constraints
                constraintSet.clear(textView.id, ConstraintSet.START)
                constraintSet.clear(textView.id, ConstraintSet.END)

                when (position % 6) {
                    0 -> {
                        // Align to start, no margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.START,
                            ConstraintSet.PARENT_ID, ConstraintSet.START
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.START, 0)
                    }

                    1 -> {
                        // Align to start with 25% margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.START,
                            ConstraintSet.PARENT_ID, ConstraintSet.START
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.START, fourth)
                    }

                    2 -> {
                        // Align to end with 25% margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.END,
                            ConstraintSet.PARENT_ID, ConstraintSet.END
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.END, fourth)
                    }

                    3 -> {
                        // Align to end, no margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.END,
                            ConstraintSet.PARENT_ID, ConstraintSet.END
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.END, 0)
                    }

                    4 -> {
                        // Align to end with 25% margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.END,
                            ConstraintSet.PARENT_ID, ConstraintSet.END
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.END, fourth)
                    }

                    5 -> {
                        // Align to start with 25% margin
                        constraintSet.connect(
                            textView.id, ConstraintSet.START,
                            ConstraintSet.PARENT_ID, ConstraintSet.START
                        )
                        constraintSet.setMargin(textView.id, ConstraintSet.START, fourth)
                    }
                }
                constraintSet.applyTo(root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadMapViewHolderV2 {
        return RoadMapViewHolderV2(
            RoadMapNodeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = milestones.size


    override fun onBindViewHolder(holder: RoadMapViewHolderV2, position: Int) {
        holder.bind(milestones[position], position)
    }
}