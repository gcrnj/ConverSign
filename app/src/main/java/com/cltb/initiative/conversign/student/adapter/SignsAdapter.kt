package com.cltb.initiative.conversign.student.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
                Glide.with(binding.image.context)
                    .load(uri)
                    .placeholder(R.drawable.loading) // 👈 Add your loading drawable here
                    .error(R.drawable.failed_loading)           // 👈 Optional: error drawable if load fails
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.image)
            }.addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to get download URL", exception)
            }


            if(position % 2 == 0) {
                binding.root.setBackgroundColor(
                    binding.root.context.getColor(R.color.grey)
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