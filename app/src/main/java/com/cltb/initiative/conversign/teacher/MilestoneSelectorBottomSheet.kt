package com.cltb.initiative.conversign.teacher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.databinding.MilestoneSelectorBottomSheetLayoutBinding
import com.cltb.initiative.conversign.teacher.adapter.TeacherSectionAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class MilestoneSelectorBottomSheet(private val context: Context) {
    private val bottomSheet = BottomSheetDialog(context)

    val binding = MilestoneSelectorBottomSheetLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        bottomSheet.setContentView(binding.root)

        // ⬇️ Enable fullscreen and dragging behavior
        binding.root.post {
            val bottomSheetView = bottomSheet.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetView?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
                behavior.isDraggable = true
                // ⬇️ Force height to match the screen
                bottomSheetView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                bottomSheetView.requestLayout()
            }
        }
    }

    fun show() {
        // Set adapters for binding.sectionsRecyclerView
        val sectionAdapter = TeacherSectionAdapter(sections)
        binding.sectionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sectionAdapter
            setHasFixedSize(true)
        }

        bottomSheet.show()
    }
}

