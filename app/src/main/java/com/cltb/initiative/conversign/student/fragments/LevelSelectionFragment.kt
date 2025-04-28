package com.cltb.initiative.conversign.student.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.SelectionData
import com.cltb.initiative.conversign.databinding.FragmentSectionBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.adapter.SelectionAdapter
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

class LevelSelectionFragment : Fragment() {

    private var _binding: FragmentSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FireStoreUtils.allLevelsCollectionRef.orderBy("id").addSnapshotListener { value, error ->
            value?.let {
                val gson = GsonBuilder().create()
                val documents = it.documents


                val selections = documents.map { doc ->
                    // Create a new JsonObject to combine document ID and data
                    val jsonObject = JsonObject().apply {
                        addProperty("documentId", doc.id)  // Add document ID
                        addProperty("type", "Level")  // Add document ID
                        doc.data?.forEach { (key, value) ->
                            add(key, gson.toJsonTree(value))  // Add all document fields
                        }
                    }

                    // Convert JsonObject to SelectionData
                    gson.fromJson(jsonObject, SelectionData::class.java)
                }

                val adapter = SelectionAdapter(selections) { section ->
                    (requireActivity() as StudentsActivity).changeFragment(
                        fragmentClass = LessonSelectionFragment::class.java,
                        args = Bundle().apply {
                            putParcelable(LessonSelectionFragment.LEVEL, section)
                        }
                    )
                }
                binding.sectionsRecyclerView.apply {
                    this.adapter = adapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }


    }


}