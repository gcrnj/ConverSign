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
import com.cltb.initiative.conversign.game_data.GameFlow
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

    private val gameFlow by lazy {
        GameFlow(this, requireActivity() as StudentsActivity)
    }

    private val section: Section? by lazy {
        val sectionNumber = arguments?.getInt(GameFlow.SECTION_NUMBER) ?: 0
        gameFlow.getSection(sectionNumber)
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
                    fragmentClass = RoadMapFragment::class.java,
                    args = Bundle().apply {
                        putInt(GameFlow.SECTION_NUMBER, section?.sectionNumber ?: -1)
                        putInt(GameFlow.LEVEL_NUMBER, level.levelNumber)
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