package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.SelectionData
import com.cltb.initiative.conversign.databinding.FragmentLevelsBinding
import com.cltb.initiative.conversign.student.adapter.SelectionAdapter
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject


class LessonSelectionFragment : Fragment() {

    private var _binding: FragmentLevelsBinding? = null
    private val binding get() = _binding!!


    private val level: SelectionData? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(LEVEL, SelectionData::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(LEVEL) as? SelectionData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        level?.let { level ->
            FireStoreUtils.lessonDocumentRef(level.documentId).orderBy("id").addSnapshotListener { value, error ->
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
                        print(jsonObject)
                        gson.fromJson(jsonObject, SelectionData::class.java)
                    }

                    val adapter = SelectionAdapter(selections) { lesson ->
                        Toast.makeText(requireContext(), "Launching Level ${level.id}, Lesson ${lesson.id}", Toast.LENGTH_SHORT).show()
                    }
                    binding.levelsRecyclerView.apply {
                        this.adapter = adapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
        }
    }
        }

    companion object {
        const val LEVEL = "LEVEL"
    }
}