package com.cltb.initiative.conversign.student.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.ChallengeMilestone
import com.cltb.initiative.conversign.data.LessonMilestone
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.databinding.FragmentLessonBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.SignsAdapter
import com.google.firebase.auth.FirebaseAuth


class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    private val progressViewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

    private val level: Level? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(LEVEL, Level::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(LEVEL)
        }
    }

    var currentChallenge = 0

    private val selectedMilestone: Milestone? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(MILESTONE, Milestone::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(MILESTONE)
        }
    }

    private val showPreviousButton: Boolean by lazy {
        arguments?.getBoolean(SHOW_PREVIOUS_BUTTON, false) ?: false
    }

    // Register permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
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
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun setObservers() {
        progressViewModel.progress.observe(viewLifecycleOwner) { progress ->

        }
    }

    private fun setClickListeners() = with(binding){
        roadmapButton.setOnClickListener {
            // Navigate to roadmap fragment
            (requireActivity() as StudentsActivity).changeFragment(
                RoadMapFragment::class.java,
                Bundle().apply {
                    putParcelable(LEVEL, level)
                }
            )
        }

        lessonNextButton.setOnClickListener { button ->
            button.isEnabled = false

            // Add 1 to current milestone
            progressViewModel.nextMilestoneLevel(FirebaseAuth.getInstance().currentUser?.uid ?: "") { newProgress ->
                val newMilestone = with(newProgress) {
                    val selectedSection = sections.find { it.sectionNumber == currentSection }
                    val selectedLevel = selectedSection?.levels?.find { it.levelNumber == currentLevel }
                    val selectedMilestone = selectedLevel?.milestones?.find { it.number == currentMilestone }
                    selectedMilestone
                }
                val newArgs = Bundle().apply {
                    putParcelable(MILESTONE, newMilestone)
                    putBoolean(SHOW_PREVIOUS_BUTTON, true)
                }
                (requireActivity() as StudentsActivity).changeFragment(
                    LessonFragment::class.java,
                    newArgs
                )
            }
        }

        challengePreviousClickListener.setOnClickListener { button ->
            button.isEnabled = false
            // Add 1 to current challenge
            progressViewModel.nextMilestoneLevel(FirebaseAuth.getInstance().currentUser?.uid ?: "") {
                button.isEnabled = true
            }
        }

        nextButton.setOnClickListener {
            // Relaunch fragment but with new parameters
            val currentProgress = progressViewModel.progress.value
//            progressViewModel.setNewProgress(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            val newArgs = Bundle().apply {
                // Check if last
//                if(level?.lessons?.size == currentLesson) {
//                    // Last lesson
//                }
//                putParcelable(LEVEL, level)
//                putInt(CURRENT_LESSON, currentLesson + 1)
//                putInt(CURRENT_CHALLENGE, 0)
//                // Add any custom arguments here
            }

            currentChallenge += 1
            updateUI()
        }
    }

    private fun updateUI() = with(binding) {
        val isLesson = selectedMilestone is LessonMilestone
        lessonsLinearLayout.visibility = if (isLesson) View.VISIBLE else View.GONE
        challengeConstraintLayout.visibility = if (isLesson) View.GONE else View.VISIBLE

        selectedMilestone ?: return@with

        if(isLesson) {
            // Update lesson
            val lesson = selectedMilestone as LessonMilestone
            gameTypeTextView.text = lesson.pageHeader
            headerSubtitleTextView.text = lesson.pageSubHeader
            signsRecyclerView.apply {
                adapter = SignsAdapter(lesson.lessons)
                layoutManager = LinearLayoutManager(requireContext())
            }
        } else {
            // Update challenge
            gameTypeTextView.text = getString(R.string.challenge)
            challengePreviousClickListener.visibility = if(showPreviousButton) View.VISIBLE else View.GONE

            val challengeMilestone = selectedMilestone as ChallengeMilestone
            val challenge = challengeMilestone.challenges[currentChallenge]
            headerSubtitleTextView.text = getString(R.string.sign_value, challenge.answer)
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.cameraView.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                Toast.makeText(requireActivity(), "Camera Binding failed", Toast.LENGTH_LONG).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    companion object {
        const val LEVEL = "LEVEL" // For roadmap
        const val MILESTONE = "CURRENT_CHALLENGE_LESSON"
        const val SHOW_PREVIOUS_BUTTON = "CURRENT_CHALLENGE_LESSON"

    }
}