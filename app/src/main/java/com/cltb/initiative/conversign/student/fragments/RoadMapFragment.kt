package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.databinding.FragmentRoadMapBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.RoadMapAdapter
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel


class RoadMapFragment : Fragment() {


    private var _binding: FragmentRoadMapBinding? = null
    private val binding get() = _binding!!

    private val level: Level? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(LEVEL, Level::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(LEVEL)
        }
    }

    private var selectedLesson: Int = 0

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
            selectedLesson = progress.currentLesson
            populateRoadMap(progress)
        }
    }

    private fun populateRoadMap(progress: Progress) {
        binding.roadmapRecyclerView.apply {
            adapter = level?.let { RoadMapAdapter(it, progress, selectedLesson) }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        const val LEVEL = "LEVEL"
    }
}