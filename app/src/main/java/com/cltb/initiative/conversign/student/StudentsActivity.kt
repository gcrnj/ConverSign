package com.cltb.initiative.conversign.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.databinding.ActivityStudentsBinding
import com.cltb.initiative.conversign.student.fragments.SectionFragment
import androidx.activity.viewModels // For Activity
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel
import com.cltb.initiative.conversign.utils.ViewModelUtils.observeOnce
import com.google.firebase.auth.FirebaseAuth

class StudentsActivity : AppCompatActivity() {

    private val binding: ActivityStudentsBinding by lazy {
        ActivityStudentsBinding.inflate(layoutInflater)
    }
    val viewModel: ProgressViewModel by viewModels()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Your custom back handling logic
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                onBackPressedDispatcher.onBackPressed()
            }

            // Show or hide back button
            // Check after popbackstack
            binding.backButton.visibility = if (supportFragmentManager.backStackEntryCount <= 2) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

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

        handleCLickListeners()
        setupViewModel()
    }

    private fun setupViewModel() {

        viewModel.loadProgressFromFireStore(FirebaseAuth.getInstance().currentUser?.uid ?: "")

        viewModel.progress.observeOnce(this) { progress ->
            progress ?: return@observeOnce

            changeFragment(
                fragmentClass = SectionFragment::class.java,
                showBackButton = false
            )
        }
        viewModel.progress.observe(this) { progress ->
            progress ?: return@observe
            setHealth(progress.healthUsed)
        }
    }

    private fun handleCLickListeners() {
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        binding.appLogo.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    StudentSettingsActivity::class.java
                )
            )
        }
        binding.backButton.setOnClickListener {
            backPressedCallback.handleOnBackPressed()
        }

    }

    // Modified fragment transaction with back stack
    fun changeFragment(
        fragmentClass: Class<out Fragment>,
        args: Bundle? = null,
        showBackButton: Boolean = true,
        popCurrentFragment: Boolean = false,
    ) {
        val fragment = fragmentClass.getDeclaredConstructor().newInstance().apply {
            arguments = args
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,   // enter (Fragment B comes in from right)
                R.anim.slide_out_left,   // exit (Fragment A moves left)
                R.anim.slide_in_left,    // popEnter (Fragment A comes in from left)
                R.anim.slide_out_right   // popExit (Fragment B moves right)
            )
            .replace(binding.selectionFrameLayout.id, fragment)
            .apply {
                addToBackStack(fragmentClass.simpleName)
                if(popCurrentFragment) {
                    supportFragmentManager.popBackStack()
                }
            }
            .commit()

        // Show back button
        binding.backButton.visibility = if (showBackButton) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun goBack() {
        supportFragmentManager.popBackStack()
    }

    private fun setHealth(health: Int) {
        binding.healthTextView.text = String.format(health.toString())
    }

}