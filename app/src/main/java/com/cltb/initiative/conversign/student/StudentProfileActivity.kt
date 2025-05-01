package com.cltb.initiative.conversign.student

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cltb.initiative.conversign.MainActivity
import com.cltb.initiative.conversign.databinding.ActivityStudentProfileBinding
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.SharedPrefUtils

class StudentProfileActivity : AppCompatActivity() {

    val binding: ActivityStudentProfileBinding by lazy {
        ActivityStudentProfileBinding.inflate(layoutInflater)
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
    }

    private fun setClickListeners() = with(binding){
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