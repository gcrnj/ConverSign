package com.cltb.initiative.conversign

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cltb.initiative.conversign.data.Roles
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.SharedPrefUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, systemBars.bottom)
            insets
        }
        val loginIntent = Intent(
            this,
            LoginActivity::class.java
        )

        findViewById<View>(R.id.beginJourneyButton).setOnClickListener { view: View? ->
            startActivity(loginIntent)
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            val role = SharedPrefUtils(this).getData(SharedPrefUtils.Keys.Role)
            role?.let {
                when (Roles.valueOf(it)) {
                    Roles.Student -> {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                StudentsActivity::class.java
                            )
                        )
                        finish()
                    }

                    Roles.Educator -> {

                    }

                    Roles.Individual -> {
                    }
                }

            } ?: run {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }
}