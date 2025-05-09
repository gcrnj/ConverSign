package com.cltb.initiative.conversign.teacher

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.databinding.MilestoneSelectorBottomSheetLayoutBinding
import com.cltb.initiative.conversign.teacher.adapter.TeacherSectionAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MilestoneSelectorBottomSheet(private val context: Context): BottomSheetDialogFragment() {


    private var _binding: MilestoneSelectorBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MilestoneSelectorBottomSheetLayoutBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

            val sectionAdapter = TeacherSectionAdapter(sections)
            binding.sectionsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = sectionAdapter
                setHasFixedSize(true)

            // ⬇️ Enable fullscreen and dragging behavior
            binding.root.post {
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.skipCollapsed = true
                    behavior.isDraggable = true
                    // ⬇️ Force height to match the screen
                    bottomSheet.requestLayout()
                }
            }
        }
        }
    }
}

