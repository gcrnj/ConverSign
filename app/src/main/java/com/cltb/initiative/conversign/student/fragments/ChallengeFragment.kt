package com.cltb.initiative.conversign.student.fragments

import android.Manifest
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
import androidx.lifecycle.lifecycleScope
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.ChallengeMilestone
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.FragmentChallengeBinding
import com.cltb.initiative.conversign.game_data.GameFlow
import com.cltb.initiative.conversign.student.StudentsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    // For progress
    private var startTimeMillis: Long = 0L
    private var secondsSpent = 0.0
    private var timerJob: Job? = null
    private var healthUsed = 0

    private val progressViewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

    private val gameFlow by lazy {
        GameFlow(this, requireActivity() as StudentsActivity)
    }

    private val selectedSection: Section? by lazy {
        val sectionNumber = arguments?.getInt(GameFlow.SECTION_NUMBER) ?: 0
        gameFlow.getSection(sectionNumber)
    }

    private val selectedLevel: Level? by lazy {
        val levelNumber = arguments?.getInt(GameFlow.LEVEL_NUMBER) ?: 0
        gameFlow.getLevel(
            currentSection = selectedSection?.sectionNumber ?: 0,
            currentLevel = levelNumber,
        )
    }

    private val selectedMilestone: ChallengeMilestone? by lazy {
        val milestoneNumber = arguments?.getInt(GameFlow.MILESTONE_NUMBER) ?: 0
        gameFlow.getMilestone(
            currentSection = selectedSection?.sectionNumber ?: 0,
            currentLevel = selectedLevel?.levelNumber ?: 0,
            currentMilestone = milestoneNumber
        ) as? ChallengeMilestone
    }

    private val randomSign by lazy {
        selectedMilestone?.challenges?.random()?.answer ?: ""
    }

    // Register permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
            startTimer()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startTimer() {
        startTimeMillis = System.currentTimeMillis()

        timerJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(100) // update every 100ms
                val elapsed = (System.currentTimeMillis() - startTimeMillis) / 1000.0
                secondsSpent = ((elapsed * 100).roundToInt()) / 100.0 // rounded to 2 decimal places
                println("User has been here for $secondsSpent seconds")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        setupOnClickListeners()
        setupTexts()
    }

    private fun setupTexts() = with(binding){
        challengeTextView.text = getString(R.string.challenge_sign, randomSign)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerJob?.cancel() // Stop timer when user leaves the fragment
    }
    private fun setupOnClickListeners() = with(binding) {

        challengePreviousButton.setOnClickListener {
            it.isEnabled = false
            gameFlow.previous(
                currentSection = selectedSection?.sectionNumber ?: return@setOnClickListener,
                currentLevel = selectedLevel?.levelNumber ?: return@setOnClickListener,
                currentMilestone = selectedMilestone?.number ?: return@setOnClickListener
            )
        }

        nextButton.setOnClickListener { button ->
            val currentProgress = progressViewModel.progress.value
            viewLifecycleOwner.lifecycleScope.launch {
                button.isEnabled = false
                gameFlow.next(
                    firebaseProgress = currentProgress ?: return@launch,
                    currentSection = selectedSection?.sectionNumber ?: return@launch,
                    currentLevel = selectedLevel?.levelNumber ?: return@launch,
                    currentMilestone = selectedMilestone?.number ?: return@launch
                ) { newProgress ->
                    // Add to database
                    progressViewModel.saveNewProgressToFireStore(
                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                        newProgress = newProgress.copy(
                            timeTaken = secondsSpent,
                            healthUsed = healthUsed,
                        ),
                        currentProgress = currentProgress,
                        timeTaken = secondsSpent,
                        healthUsed = healthUsed,
                    )
                }
            }
        }

        cameraView.setOnClickListener {
            binding.niceJobLinearLayout.visibility = View.VISIBLE
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

}