package com.cltb.initiative.conversign.student

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
import com.cltb.initiative.conversign.student.fragments.LevelSelectionFragment
import com.cltb.initiative.conversign.student.fragments.SectionFragment

class StudentsActivity : AppCompatActivity() {

    private val binding: ActivityStudentsBinding by lazy {
        ActivityStudentsBinding.inflate(layoutInflater)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Your custom back handling logic
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                onBackPressedDispatcher.onBackPressed()
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

        // First Load
        changeFragment(SectionFragment::class.java)
    }

    private fun handleCLickListeners() {
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        binding.appLogo.setOnClickListener {
            // Todo - Go to student profile view and add logout
        }

    }

    // Modified fragment transaction with back stack
    fun changeFragment(fragmentClass: Class<out Fragment>, args: Bundle? = null) {
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
            }
            .commit()
    }

}