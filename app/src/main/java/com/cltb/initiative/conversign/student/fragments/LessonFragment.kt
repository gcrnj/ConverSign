package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.LessonMilestone
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.FragmentLessonBinding
import com.cltb.initiative.conversign.game_data.GameFlow
import com.cltb.initiative.conversign.game_data.GameFlow.Companion.LEVEL_NUMBER
import com.cltb.initiative.conversign.game_data.GameFlow.Companion.MILESTONE_NUMBER
import com.cltb.initiative.conversign.game_data.GameFlow.Companion.SECTION_NUMBER
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.SignsAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    private val progressViewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

    private val gameFlow by lazy {
        GameFlow(this, requireActivity() as StudentsActivity)
    }

    private val selectedSection: Section? by lazy {
        val sectionNumber = arguments?.getInt(SECTION_NUMBER) ?: 0
        gameFlow.getSection(sectionNumber)
    }

    private val selectedLevel: Level? by lazy {
        val levelNumber = arguments?.getInt(LEVEL_NUMBER) ?: 0
        gameFlow.getLevel(
            currentSection = selectedSection?.sectionNumber ?: 0,
            currentLevel = levelNumber,
        )
    }

    private val selectedMilestone: Milestone? by lazy {
        val milestoneNumber = arguments?.getInt(MILESTONE_NUMBER) ?: 0
        gameFlow.getMilestone(
            currentSection = selectedSection?.sectionNumber ?: 0,
            currentLevel = selectedLevel?.levelNumber ?: 0,
            currentMilestone = milestoneNumber
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        updateUI()
//        setObservers()
    }

    private fun setObservers() {
        progressViewModel.progress.observe(viewLifecycleOwner) { progress ->

        }
    }

    private fun setClickListeners() = with(binding) {
        roadmapButton.setOnClickListener {
            // Navigate to roadmap fragment
            (requireActivity() as StudentsActivity).goBack()
        }

        lessonNextButton.setOnClickListener { button ->
            button.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                val currentProgress = progressViewModel.progress.value
                gameFlow.next(
                    firebaseProgress = currentProgress ?: return@launch,
                    currentSection = selectedSection?.sectionNumber ?: return@launch,
                    currentLevel = selectedLevel?.levelNumber ?: return@launch,
                    currentMilestone = selectedMilestone?.number ?: return@launch
                ) { newProgress ->
                    // Add to database
                    progressViewModel.saveNewProgressToFireStore(
                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                        currentProgress = currentProgress,
                        newProgress = newProgress,
                    )
                }
            }
        }

        lessonPreviousButton.setOnClickListener { button ->
            button.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                gameFlow.previous(
                    currentSection = selectedSection?.sectionNumber ?: return@launch,
                    currentLevel = selectedLevel?.levelNumber ?: return@launch,
                    currentMilestone = selectedMilestone?.number ?: return@launch
                )
            }
        }

//        challengePreviousClickListener.setOnClickListener { button ->
//            button.isEnabled = false
//            // Add 1 to current challenge
//            progressViewModel.nextMilestoneLevel(FirebaseAuth.getInstance().currentUser?.uid ?: "") {
//                button.isEnabled = true
//            }
//        }
//
//        nextButton.setOnClickListener {
//            // Relaunch fragment but with new parameters
//            val currentProgress = progressViewModel.progress.value
////            progressViewModel.setNewProgress(FirebaseAuth.getInstance().currentUser?.uid ?: "")
//            val newArgs = Bundle().apply {
//                // Check if last
////                if(level?.lessons?.size == currentLesson) {
////                    // Last lesson
////                }
////                putParcelable(LEVEL, level)
////                putInt(CURRENT_LESSON, currentLesson + 1)
////                putInt(CURRENT_CHALLENGE, 0)
////                // Add any custom arguments here
//            }
//
//            currentChallenge += 1
//            updateUI()
//        }
    }

    private fun updateUI() = with(binding) {
        lessonPreviousButton.visibility = if ((selectedMilestone?.number ?: 0) > 1) View.VISIBLE else View.GONE
        selectedMilestone ?: return@with

        // Update lesson
        val lesson = selectedMilestone as LessonMilestone
        gameTypeTextView.text = lesson.pageHeader
        headerSubtitleTextView.text = lesson.pageSubHeader
        signsRecyclerView.apply {
            adapter = SignsAdapter(lesson.lessons)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}