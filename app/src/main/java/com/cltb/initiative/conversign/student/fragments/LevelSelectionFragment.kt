package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.databinding.FragmentLevelSelectionBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.LevelSelectionAdapter

class LevelSelectionFragment : Fragment() {

    companion object {
        const val SECTION = "SECTION"
    }

    private var _binding: FragmentLevelSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

    private val section: Section? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(SECTION, Section::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(SECTION)
        }
    }

    private val levels by lazy {
        section?.levels.orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLevelSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTexts()
        observeViewModel()

    }

    private fun setTexts() {
        binding.selectedSectionTextView.text = section?.name.orEmpty()
    }

    private fun observeViewModel() {

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progress ?: return@observe

            val adapter = LevelSelectionAdapter(levels, progress) { level ->
                (requireActivity() as StudentsActivity).changeFragment(
                    fragmentClass = LessonFragment::class.java,
                    args = Bundle().apply {
                        putParcelable(LessonFragment.LEVEL, level)
                        val isCurrentSection = progress.currentSection == section?.sectionNumber
                        val isCurrentLevel = progress.currentLevel == level.levelNumber
                        val isInLatestSelection = isCurrentLevel && isCurrentSection
                        val currentMilestone = if (isInLatestSelection) {
                            level.milestones.find { it.number == progress.currentMilestone }
                        } else {
                            level.milestones.first()
                        }
                        putParcelable(LessonFragment.MILESTONE, currentMilestone)
                    }
                )
            }
            binding.levelsRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }


}