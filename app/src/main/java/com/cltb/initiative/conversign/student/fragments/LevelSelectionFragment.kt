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

        val adapter = LevelSelectionAdapter(levels) { level ->
            (requireActivity() as StudentsActivity).changeFragment(
                fragmentClass = LessonFragment::class.java,
                args = Bundle().apply {
                    putParcelable(LessonFragment.LEVEL, level)
                }
            )
        }
        binding.selectedSectionTextView.text = section?.name.orEmpty()
            binding.levelsRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
            }

    }


}