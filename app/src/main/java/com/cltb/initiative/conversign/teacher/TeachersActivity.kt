package com.cltb.initiative.conversign.teacher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cltb.initiative.conversign.MainActivity
import com.cltb.initiative.conversign.databinding.ActivityTeachersBinding
import com.cltb.initiative.conversign.teacher.adapter.StudentsAdapter
import com.cltb.initiative.conversign.teacher.viewmodels.TeachersViewModel
import com.cltb.initiative.conversign.utils.SharedPrefUtils

class TeachersActivity : AppCompatActivity() {

    private val binding: ActivityTeachersBinding by lazy {
        ActivityTeachersBinding.inflate(layoutInflater)
    }

    private val teachersViewModel: TeachersViewModel by viewModels()

    private val bottomSheet by lazy {
        MilestoneSelectorBottomSheet(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top,
                systemBars.right + v.paddingRight,
                systemBars.bottom
            )
            insets
        }

        setClickListeners()
        setupObservers()

    }

    private fun setClickListeners() = with(binding) {
        logoutButton.setOnClickListener {
            SharedPrefUtils(this@TeachersActivity).logout()
            startActivity(
                Intent(
                    this@TeachersActivity,
                    MainActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }

        levelText.setOnClickListener {
            bottomSheet.show(
                supportFragmentManager, "MilestoneSelector",
            )
        }
    }


    private fun setupObservers() {
        teachersViewModel.fetchTeacherData()
        teachersViewModel.students.observe(this) { students ->
            Toast.makeText(this, students.size.toString(), Toast.LENGTH_SHORT).show()
            if (students.isEmpty()) {
                // No students in the class code
                binding.noProgressTextView.visibility = View.VISIBLE
                binding.studentsRecyclerView.visibility = View.GONE
            } else {
                binding.noProgressTextView.visibility = View.GONE
                binding.studentsRecyclerView.visibility = View.VISIBLE
                binding.studentsRecyclerView.apply {
                    adapter = StudentsAdapter(students) {
                        Toast.makeText(this@TeachersActivity, it.fullName(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    layoutManager = LinearLayoutManager(this@TeachersActivity)
                }
            }
        }
        teachersViewModel.educator.observe(this) {
            binding.classCodeTextView.text = it.classCode
            teachersViewModel.fetchStudents(it.classCode)

        }
    }
}