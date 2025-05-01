package com.cltb.initiative.conversign.student.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.levels
import com.cltb.initiative.conversign.databinding.FragmentSectionBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.SelectionAdapter

class LevelSelectionFragment : Fragment() {

    private var _binding: FragmentSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SelectionAdapter(levels) { section ->
            (requireActivity() as StudentsActivity).changeFragment(
                fragmentClass = LessonFragment::class.java,
                args = Bundle().apply {
                    putParcelableArrayList(LessonFragment.LESSON, ArrayList(section.lessons))
                }
            )
        }
        binding.sectionsRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }


}