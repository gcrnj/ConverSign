package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.databinding.StudentItemLayoutBinding
import com.cltb.initiative.conversign.utils.FirebaseStorageUtils

class StudentsAdapter(
    private val students: List<Student>,
    private val section: Section,
    private val level: Level,
    private val onStudentClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {

    inner class StudentsViewHolder(
        private val binding: StudentItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) = with(binding) {
            fullNameTextView.text = student.fullName()
            setProgress(student)
            root.setOnClickListener {
                onStudentClick(student)
            }

            // Imageview
            val studentRef = FirebaseStorageUtils.studentProfileRef(student.id)
            studentRef.downloadUrl.addOnSuccessListener { uri ->
                binding.profileImageView.load(uri)
            }
        }

        private fun setProgress(student: Student) = with(binding) {
            // Section > Level
            // selectedSection , selectedLevel
            // student.section , student.level
            val studentSectionNumber = student.progress?.section ?: 0
            val studentLevelNumber = student.progress?.level ?: 0
            val selectedSectionNumber = section.sectionNumber
            val selectedLevelNumber = level.levelNumber
            val (text, colorResId) = when {
                (studentSectionNumber > selectedSectionNumber) ||
                        (studentSectionNumber == selectedSectionNumber &&
                                studentLevelNumber > selectedLevelNumber) -> {
                    "Done" to R.color.done_color
                }

                studentSectionNumber < selectedSectionNumber || studentLevelNumber < selectedLevelNumber
                     -> {
                    "No Activity" to R.color.no_activity_color
                }

                else -> {
                    "In Progress" to R.color.in_progress_color
                }
            }

            progressTextView.text = text
            progressTextView.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    colorResId
                )
            )
//            if (studentSectionNumber > selectedSectionNumber) {
//                progressTextView.text = "Done"
//                progressTextView.setTextColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.done_color
//                    )
//                )
//            } else if (studentSectionNumber < selectedSectionNumber) {
//                progressTextView.text = "No Activity"
//                progressTextView.setTextColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.no_activity_color
//                    )
//                )
//            } else if (
//            // At this point, student is in current section
//                studentLevelNumber > selectedLevelNumber
//            ) {
//                progressTextView.text = "Done"
//                progressTextView.setTextColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.done_color
//                    )
//                )
//            } else if (
//                studentLevelNumber < selectedLevelNumber
//            ) {
//                progressTextView.text = "No Activity"
//                progressTextView.setTextColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.no_activity_color
//                    )
//                )
//            } else {
//                progressTextView.text = "In Progress"
//                progressTextView.setTextColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.in_progress_color
//                    )
//                )
//            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        return StudentsViewHolder(
            StudentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }
}