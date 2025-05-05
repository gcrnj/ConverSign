package com.cltb.initiative.conversign.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cltb.initiative.conversign.MainActivity
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.databinding.ActivityStudentProfileBinding
import com.cltb.initiative.conversign.student.viewmodels.StudentProfileViewModel
import com.cltb.initiative.conversign.utils.SharedPrefUtils

class StudentProfileActivity : AppCompatActivity() {

    val binding: ActivityStudentProfileBinding by lazy {
        ActivityStudentProfileBinding.inflate(layoutInflater)
    }
    val viewModel: StudentProfileViewModel by viewModels()

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
        setupObservers()
        setClickListeners()
    }

    private fun setupObservers() {
        viewModel.getStudent()
        viewModel.student.observe(this) { student ->
            updateUI(student)
        }
    }

    private fun updateUI(student: Student?) = with(binding) {
        val buttonsVisible = if (student != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
        editProfileButton.visibility = buttonsVisible
        logoutButton.visibility = buttonsVisible

        student ?: return
        firstNameTextView.text = student.firstName
        lastNameTextView.text = student.lastName
        roleTextView.text = student.role()
        emailTextView.text = student.email
        phoneNumberTextView.text = student.phone
        classCodeTextView.text = student.classCode
        // createdAtTextView.text = getString(R.string.created_at, student.formattedCreatedAt())

    }

    private fun setClickListeners() = with(binding) {
        logoutButton.setOnClickListener {
            SharedPrefUtils(this@StudentProfileActivity).logout()
            startActivity(
                Intent(
                    this@StudentProfileActivity,
                    MainActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }
    }
}