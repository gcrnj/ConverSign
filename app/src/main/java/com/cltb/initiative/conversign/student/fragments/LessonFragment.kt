package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.databinding.FragmentLevelsBinding


class LessonFragment : Fragment() {

    private var _binding: FragmentLevelsBinding? = null
    private val binding get() = _binding!!

    private var currentLesson = 1
    private var isLessonCompleted = false

    private val lessons: List<Lesson>? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(LESSON, Lesson::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelableArrayList(LESSON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    private fun updateUI() = with(binding) {
        niceJobLinearLayout.visibility = View.GONE
        lessonsLinearLayout.visibility = if (isLessonCompleted) View.GONE else View.VISIBLE
        challengeConstraintLayout.visibility = if (isLessonCompleted) View.VISIBLE else View.GONE

        val lesson = lessons?.find { it.lessonNumber == currentLesson }
        lesson?.let {
            // Lesson or Challenge?
            val challenge = it.lessonChallenges.getOrNull(currentLesson)

            if (!isLessonCompleted) {
                // Lesson first
                gameTypeTextView.text = getString(R.string.lesson_number, lesson.lessonNumber)
                headerSubtitleTextView.text = lesson.description
            } else {
                // Challenge now
                challenge?.let { safeChallenge ->
                    gameTypeTextView.text = getString(R.string.challenge)
                    headerSubtitleTextView.text = safeChallenge.challengeHint
                }
            }
        } ?: run {
            // Done
        }
    }

    companion object {
        const val LESSON = "LESSON"
    }
}