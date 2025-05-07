package com.cltb.initiative.conversign.student

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.databinding.ActivityStudentSettingsBinding
import com.cltb.initiative.conversign.game_data.GameFlow
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel
import com.cltb.initiative.conversign.student.viewmodels.StudentProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class StudentSettingsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityStudentSettingsBinding.inflate(layoutInflater)
    }

    val gameFlow = GameFlow()

    private val studentViewModel by viewModels<StudentProfileViewModel>()
    private val progressViewModel by viewModels<ProgressViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top,
                systemBars.right + v.paddingRight,
                systemBars.bottom
            )
            insets
        }

        setupToolbar()
        setupObservers()
    }

    private fun setupObservers() {
        studentViewModel.getStudent(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        progressViewModel.loadProgressFromFireStore(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        studentViewModel.student.observe(this) { profile ->
            binding.profileName.text = profile?.fullName() ?: ""
        }
        progressViewModel.progress.observe(this) { progress ->
            val currentSection = gameFlow.getSection(progress?.currentSection ?: 0)
            val sectionName = currentSection?.javaClass?.simpleName ?: ""
            binding.profileSection.text = getString(R.string.level_value, sectionName)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbarLayout.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        toolbar.title = getString(R.string.settings)
    }
}