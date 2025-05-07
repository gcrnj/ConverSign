package com.cltb.initiative.conversign.student.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.databinding.LessonLayoutBinding
import com.google.firebase.storage.FirebaseStorage

class SignsAdapter(private val lessons: List<Lesson>): RecyclerView.Adapter<SignsAdapter.SignsViewHolder>() {

    inner class  SignsViewHolder(val binding: LessonLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson, position: Int) {
            binding.signNameTextView.text = lesson.signName
            binding.lessonHintTextView.text = lesson.signHint
//            val storageRef = FirebaseStorage.getInstance().getReference()

            val storageRef = FirebaseStorage.getInstance().getReference(lesson.signFirebaseImage)

            storageRef.downloadUrl.addOnSuccessListener { uri ->
                binding.image.load(uri)
            }.addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to get download URL", exception)
            }


            if(position % 2 == 1) {
                binding.root.setBackgroundColor(
                    binding.root.context.getColor(R.color.grey)
                )
            } else {
                binding.root.setBackgroundColor(
                    binding.root.context.getColor(R.color.screen_bg)
                )
            }
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

    override fun getItemCount(): Int {
        val length = lessons.size // = 4
        return length
    }

    override fun onBindViewHolder(holder: SignsViewHolder, position: Int) {
        val lesson = lessons[position] // Why is position always 0-2?
        holder.bind(lesson, position)
    }
}