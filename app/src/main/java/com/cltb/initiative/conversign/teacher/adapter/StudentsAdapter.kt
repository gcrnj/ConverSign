package com.cltb.initiative.conversign.teacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.databinding.StudentItemLayoutBinding
import com.cltb.initiative.conversign.utils.FirebaseStorageUtils

class StudentsAdapter(
    private val students: List<Student>,
    private val onStudentClick: (Student) -> Unit
): RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {

    inner class StudentsViewHolder(
        private val binding: StudentItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) = with(binding){
            fullNameTextView.text = student.fullName()
            progressTextView.text = "In Progress"
            progressTextView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.in_progress_color))
            root.setOnClickListener {
                onStudentClick(student)
            }

            // Imageview
            val studentRef = FirebaseStorageUtils.studentProfileRef(student.id)
            studentRef.downloadUrl.addOnSuccessListener { uri ->
                binding.profileImageView.load(uri) {
                    placeholder(R.drawable.profile_sample) // shown while loading
                    error(R.drawable.profile_sample)         // shown if loading fails
                }
            }
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