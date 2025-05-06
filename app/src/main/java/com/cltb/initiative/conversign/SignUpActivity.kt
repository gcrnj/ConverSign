package com.cltb.initiative.conversign

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.cltb.initiative.conversign.data.Roles
import com.cltb.initiative.conversign.databinding.ActivitySignUpBinding
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.teacher.TeachersActivity
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.SharedPrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val emailFromIntent: String by lazy {
        intent.getStringExtra("email") ?: ""
    }

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
        binding.apply {
//            firstNameEditText.setText("Jay")
//            lastNameEditText.setText("Jay")
//            phoneEditText.setText("09123456789")
//            emailEditText.setText("gio.cornejo@my.jru.edu")
//            passwordEditText.setText("123456789")
//            confirmPasswordEditText.setText("123456789")
        }
        if (emailFromIntent.isNotBlank()) {
            binding.emailLayout.isEnabled = false
            binding.emailEditText.setText(emailFromIntent)
        }

        handleClickListeners()
    }

    private fun handleClickListeners() {
        with(binding) {
            roleLayout.roleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                studentClassCodeEditText.visibility =
                    if (checkedId == roleLayout.studentRadioButton.id) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
            tncTextView.setOnClickListener {
                // Todo - Launch tnc page
            }
            createAccountButton.setOnClickListener {
                it.isEnabled = false
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                // val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val password = passwordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()
                val role = getSelectedRole()
                val isUserAuthenticated = emailFromIntent.isNotBlank()
                val classCode =
                    if (role == Roles.Student.name) studentClassCodeEditText.text.toString() else generateNewClassCode()


                lifecycleScope.launch {

                    // Validate
                    val isSignUpValid = validateRegister(
                        firstName,
                        lastName,
                        // username,
                        email,
                        phone,
                        password,
                        confirmPassword,
                        classCode,
                        role
                    )

                    if (!isSignUpValid) {
                        it.isEnabled = true
                        return@launch
                    }

                    var newClassCode = classCode
                    if (getSelectedRole() == Roles.Student.name) {
                        // Check class code first in firestore
                        if (!classCodeExists(newClassCode)) {
                            // Invalid class code
                            showError(getString(R.string.cannot_find_class_code))
                            it.isEnabled = true
                            return@launch
                        }
                    } else if (getSelectedRole() == Roles.Educator.name) {
                        // Generate a new classCode while the classCode exists
                        newClassCode = generateNewClassCode()
                        while (classCodeExists(newClassCode)) {
                            newClassCode = generateNewClassCode()
                        }
                    } else {
                        it.isEnabled = true
                        return@launch
                    }

                    // Signup
                    registerAccount(
                        firstName,
                        lastName,
                        // username,
                        email,
                        phone,
                        role,
                        password,
                        newClassCode,
                        isUserAuthenticated
                    )
                }

            }
        }
    }

    private fun generateNewClassCode(): String {
        // Pattern: {2 numbers}-{5 alphabets}
        // Random characters
        val alphabets = ('a'..'z') + ('A'..'Z')
        val randomAlphabets = (1..5).map { alphabets.random() }.joinToString("")
        // Random numbers
        val randomNumbers = (1..2).map { (0..9).random() }.joinToString("")
        return "$randomNumbers-$randomAlphabets"
    }

    private fun registerAccount(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        role: String,
        password: String,
        classCode: String,
        userAuthenticated: Boolean,
    ) {
        if (!userAuthenticated) {
            // Register auth first
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    createFireStoreData(
                        firstName,
                        lastName,
                        email,
                        phone,
                        classCode,
                        role,
                    )
                }
                .addOnFailureListener { e ->
                    val errorMessage = when (e) {
                        is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_credentials_please_try_again)
                        is FirebaseAuthInvalidUserException -> "No account found with this email."
                        is FirebaseAuthUserCollisionException -> "An account already exists with this email."
                        else -> e.localizedMessage ?: "Login failed"
                    }
                    showError(errorMessage)
                    binding.createAccountButton.isEnabled = true
                }
                .addOnCanceledListener {
                    showError("Registration canceled")
                    binding.createAccountButton.isEnabled = true
                }
        } else {
            createFireStoreData(
                firstName,
                lastName,
                email,
                phone,
                classCode,
                role,
            )
        }
    }

    // By this time, the user is already authenticated
    private fun createFireStoreData(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        classCode: String,
        role: String,
    ) {

        val data = with(FireStoreUtils.StudentKeys) {
            mapOf(
                FIRST_NAME to firstName,
                LAST_NAME to lastName,
                EMAIL to email,
                PHONE to phone,
                CLASS_CODE to classCode,
                CREATED_AT to FieldValue.serverTimestamp() // Using Firestore's server timestamp
            )
        }
        FireStoreUtils
            .currentUserCollectionRef(role)
            .set(data)
            .addOnSuccessListener {
                // Start the next activity
                SharedPrefUtils(this).saveData(
                    SharedPrefUtils.Keys.Role,
                    role
                )
                when (role) {
                    Roles.Student.name -> StudentsActivity::class.java
                    Roles.Educator.name -> TeachersActivity::class.java
                    Roles.Individual.name -> null // Todo - add the educator's activity
                    else -> null
                }?.let { nextActivity ->
                    startActivity(Intent(this, nextActivity))
                    finish()
                }
            }
            .addOnFailureListener {
                val errorMessage = when (it) {
                    is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_credentials_please_try_again)
                    is FirebaseAuthInvalidUserException -> "No account found with this email."
                    is FirebaseAuthUserCollisionException -> "An account already exists with this email."
                    else -> it.localizedMessage ?: "Login failed"
                }
                showError(errorMessage)
                binding.createAccountButton.isEnabled = true
            }
            .addOnCanceledListener {
                showError("Failed to create user data. Please try again.")
                binding.createAccountButton.isEnabled = true
            }
    }

    private suspend fun classCodeExists(classCode: String): Boolean {
        return try {
            val snapshot = FireStoreUtils.allEducatorsCollectionRef
                .whereEqualTo("classCode", classCode)
                .get()
                .await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    private fun validateRegister(
        firstName: String,
        lastName: String,
        // username: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
        classCode: String,
        role: String
    ): Boolean {
        var isValidated = true
        // First Name
        if (firstName.isBlank()) {
            binding.firstNameLayout.error = "First name cannot be empty"
            isValidated = false
        } else {
            binding.firstNameLayout.error = null
        }
        // Last Name
        if (lastName.isBlank()) {
            binding.lastNameLayout.error = "Last name cannot be empty"
            isValidated = false
        } else {
            binding.lastNameLayout.error = null
        }

        // Username
        /*if (username.isBlank()) {
            binding.usernameLayout.error = "Username cannot be empty"
            return
        } else {
            binding.usernameLayout.error = null
        }
         */

        // Email
        if (email.isBlank()) {
            binding.emailLayout.error = "Email cannot be empty"
            isValidated = false
        } else if (!email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))) {
            binding.emailLayout.error = "Invalid email format"
            isValidated = false
        } else {
            binding.emailLayout.error = null
        }

        // Phone
        if (phone.isBlank()) {
            binding.phoneLayout.error = "Phone number cannot be empty"
        } else if (phone.length < 11) {
            binding.phoneLayout.error = "Phone numbers should be 11 digits"
            isValidated = false
        } else if (!phone.startsWith("09")) {
            binding.phoneLayout.error = "Only valid phone numbers (09...) are allowed"
            isValidated = false
        } else {
            binding.phoneLayout.error = null
        }

        // Password
        if (password.isBlank()) {
            binding.passwordLayout.error = "Password cannot be empty"
            isValidated = false
        } else {
            binding.passwordLayout.error = null
        }
        // Confirm Password
        if (confirmPassword.isBlank()) {
            binding.confirmPasswordLayout.error = "Confirm password cannot be empty"
            isValidated = false
        } else {
            binding.confirmPasswordLayout.error = null
        }

        // Tnc checkbox
        if (!binding.tncCheckBox.isChecked) {
            showError("We cannot proceed unless you accept the Terms and Conditions.")
            isValidated = false
        }

        // Role (and `classCode`)
        if (role.isBlank()) {
            showError(getString(R.string.role_cannot_be_empty))
            isValidated = false
        } else if (role == Roles.Student.name && classCode.isBlank()) {
            showError(getString(R.string.class_code_cannot_be_empty))
            isValidated = false
        }

        return isValidated
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

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
    }
}