package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.databinding.RoadMapNodeLayoutBinding
import com.cltb.initiative.conversign.databinding.RoadMapNodeSetLayoutBinding

class RoadMapAdapter(
    private val level: Level,
    private val progress: Progress,
    private val selectedLesson: Int,
) :
    RecyclerView.Adapter<RoadMapAdapter.RoadMapViewHolder>() {

    // lessons is now a 3d list
    // List<List<Lesson>>
    // It is a list of 10 lists max each
    private val lessons = level.lessons.chunked(10)

    inner class RoadMapViewHolder(private val viewBinding: RoadMapNodeSetLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        private val nodeBindings = listOf(
            viewBinding.node1,
            viewBinding.node2,
            viewBinding.node3,
            viewBinding.node4,
            viewBinding.node5,
            viewBinding.node6,
            viewBinding.node7,
            viewBinding.node8
        )

        fun bind(lessons: List<Lesson>) {
            nodeBindings.forEachIndexed { index, binding ->
                val lesson = lessons.getOrNull(index)

                // Enable only if:
                // progress level == current level && progress lesson >= lesson number
                // else - progress level should be greater than current level
                val isProgressDone = if (progress.currentLevel == level.levelNumber) {
                    progress.currentLesson >= (lesson?.lessonNumber ?: 0)
                } else {
                    progress.currentLevel > level.levelNumber
                }

                val isSelected = lesson?.lessonNumber == selectedLesson
                lesson?.let {
                    // Enable or disable
                    binding.root.text = lesson.description
                    binding.root.setOnClickListener {
                        // Navigate to lesson
                    }
                    if (isSelected && isProgressDone) {
                        binding.root.setBackgroundResource(R.drawable.roadmap_node_selected)
                        binding.root.setTextColor(ContextCompat.getColor(binding.root.context, R.color.screen_bg))
                    } else if (!isProgressDone) {
                        binding.root.setBackgroundResource(R.drawable.roadmap_node_locked)
                        binding.root.setTextColor(ContextCompat.getColor(binding.root.context, R.color.screen_bg))
                    }
                } ?: run {
                    binding.root.visibility = View.INVISIBLE
                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadMapViewHolder {
        return RoadMapViewHolder(
            RoadMapNodeSetLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = lessons.size


    override fun onBindViewHolder(holder: RoadMapViewHolder, position: Int) {
        holder.bind(lessons[position])
    }
}