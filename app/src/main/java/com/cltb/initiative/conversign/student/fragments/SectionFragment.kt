package com.cltb.initiative.conversign.student.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.databinding.FragmentSectionBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.SectionSelectionAdapter
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SectionFragment : Fragment() {

    val viewModel: ProgressViewModel by lazy {
        (requireActivity() as StudentsActivity).viewModel
    }

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

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progress ?: return@observe

            val adapter = SectionSelectionAdapter(sections, progress) { section ->
                (requireActivity() as StudentsActivity).changeFragment(
                    fragmentClass = LevelSelectionFragment::class.java,
                    args = Bundle().apply {
                        putParcelable(LevelSelectionFragment.SECTION, section)
                    }
                )
            }
            binding.sectionsRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}