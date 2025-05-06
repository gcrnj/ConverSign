package com.cltb.initiative.conversign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cltb.initiative.conversign.data.Roles
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.databinding.ActivityLoginBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.teacher.TeachersActivity
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.SharedPrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
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

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.signUpButton.setOnClickListener {
            val intent = Intent(
                this,
                SignUpActivity::class.java
            )
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener { v ->
            val selectedRole = getSelectedRole()
            if (selectedRole.isEmpty()) {
                showError(getString(R.string.please_select_a_role))
                return@setOnClickListener
            }
            val button = v as Button
            button.isEnabled = false
            binding.apply {
                errorTextView.visibility = View.GONE
                loginProgressBar.visibility = View.VISIBLE
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (email.isBlank() || password.isBlank()) {
                    showError(getString(R.string.please_enter_email_or_password))
                    button.isEnabled = true
                    return@apply
                }
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        updateUI(email, selectedRole)
                    }
                    .addOnFailureListener { e ->
                        val errorMessage = when (e) {
                            is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_credentials_please_try_again)
                            is FirebaseAuthInvalidUserException -> "No account found with this email."
                            is FirebaseAuthUserCollisionException -> "An account already exists with this email."
                            else -> e.localizedMessage ?: "Login failed"
                        }
                        showError(errorMessage)
                        button.isEnabled = true
                    }
                    .addOnCanceledListener {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login canceled.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun updateUI(email: String, selectedRole: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        FireStoreUtils
            .userCollectionRef(
                role = selectedRole,
                userId = userId
            )
            .get() // Retrieve all documents with that role
            .addOnCompleteListener { snapshot ->
                snapshot.result?.data?.let { data ->
                    // Found the current user's document, parse it
                    SharedPrefUtils(this).saveData(
                        SharedPrefUtils.Keys.Role,
                        selectedRole
                    )
                    when (Roles.valueOf(selectedRole)) {
                        Roles.Student -> {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    StudentsActivity::class.java
                                )
                            )
                            finish()
                        }

                        Roles.Educator -> {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    TeachersActivity::class.java
                                )
                            )
                            finish()
                        }

                        Roles.Individual -> {
                        }
                    }
                } ?: run {
                    // Logged in but no document was found for the current user
                    // Might be other roles
                    showError(getString(R.string.invalid_credentials_please_try_again))
//                    Log.d("updateUI", "No user document found with the selected role.")
//                    Log.d("updateUI", "Exception: ${snapshot.exception}")
//                    Log.d("updateUI", "Starting SignUpActivity")
//                    val intent = Intent(
//                        this,
//                        SignUpActivity::class.java
//                    ).putExtra("email", email)
//                    startActivity(intent)
                    binding.loginButton.isEnabled = true
                    binding.loginProgressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors during the query
                FirebaseAuth.getInstance().signOut()
                val errorMessage = "Error fetching user data"
                Log.e("updateUI", "", exception)
                showError(errorMessage)
                binding.loginButton.isEnabled = true
            }

    }

    private fun showError(message: String) {
        with(binding) {
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
            loginProgressBar.visibility = View.GONE
        }
    }

    private fun getSelectedRole(): String {
        return with(binding) {
            when {
                roleLayout.studentRadioButton.isChecked -> Roles.Student.name
                roleLayout.educatorRadioButton.isChecked -> Roles.Educator.name
                roleLayout.individualRadioButton.isChecked -> Roles.Individual.name
                else -> ""
            }
        }
    }
}