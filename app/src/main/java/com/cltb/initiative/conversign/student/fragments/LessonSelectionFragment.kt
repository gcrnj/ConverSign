package com.cltb.initiative.conversign.student.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.databinding.FragmentLevelsBinding
import com.cltb.initiative.conversign.student.adapter.SelectionAdapter
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject


class LessonSelectionFragment : Fragment() {

    private var _binding: FragmentLevelsBinding? = null
    private val binding get() = _binding!!


    private val lessons: List<Lesson>? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(LESSON, Lesson::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelableArrayList(LESSON)
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

        lessons?.let {


        }
    }

    companion object {
        const val LESSON = "LESSON"
    }
}