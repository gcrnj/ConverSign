package com.cltb.initiative.conversign.student.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.databinding.LessonLayoutBinding
import com.google.firebase.storage.FirebaseStorage

class SignsAdapter(private val lessons: List<Lesson>): RecyclerView.Adapter<SignsAdapter.SignsViewHolder>() {

    inner class  SignsViewHolder(val binding: LessonLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.signNameTextView.text = lesson.signName
            binding.lessonHintTextView.text = lesson.signHint
            val storageRef = FirebaseStorage.getInstance().getReference(lesson.signFirebaseImage)


            Glide.with(binding.image.context)
                .load(storageRef)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignsViewHolder {
        return SignsViewHolder(
            LessonLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = lessons.size + 1

    override fun onBindViewHolder(holder: SignsViewHolder, position: Int) {
        holder.bind(lessons[position])
    }
}