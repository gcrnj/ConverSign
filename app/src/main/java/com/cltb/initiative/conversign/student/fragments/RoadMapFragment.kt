package com.cltb.initiative.conversign.student.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.FragmentRoadMapBinding
import com.cltb.initiative.conversign.game_data.GameFlow
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.RoadMapAdapter
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel

/***
 * Roadmap requires [GameFlow.SECTION_NUMBER] and [GameFlow.LEVEL_NUMBER]
 */
class RoadMapFragment : Fragment() {

    private var _binding: FragmentRoadMapBinding? = null
    private val binding get() = _binding!!

    private val gameFlow by lazy {
        GameFlow(this, requireActivity() as StudentsActivity)
    }

    private val selectedSection: Section? by lazy {
        val section = arguments?.getInt(GameFlow.SECTION_NUMBER) ?: 0
        gameFlow.getSection(section)
    }

    private val selectedLevel: Level? by lazy {
        val sectionNumber = selectedSection?.sectionNumber
        val levelNumber = arguments?.getInt(GameFlow.LEVEL_NUMBER) ?: 0
        gameFlow.getLevel(sectionNumber ?: 0, levelNumber)
    }

    private var currentLesson: Int = 0

    val viewModel: ProgressViewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRoadMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progress ?: return@observe
            if (progress.currentSection == (selectedSection?.sectionNumber ?: 0)) {
                currentLesson = progress.currentMilestone
            } else {
                currentLesson = selectedLevel?.milestones?.size ?: 0
            }
            populateRoadMap(progress)
        }
    }

    private fun populateRoadMap(progress: Progress) {
        binding.roadmapRecyclerView.apply {
            adapter = selectedLevel?.let {
                RoadMapAdapter(it, progress, currentLesson) { milestone ->
                    // On Click
                    // Navigate to lesson
                    gameFlow.changeFragment(
                        milestone = milestone,
                        currentSection = progress.currentSection,
                        currentLevel = progress.currentLevel,
                        currentMilestone = milestone.number,
                        popCurrentFragment = false,
                    )
                }
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
    }
}