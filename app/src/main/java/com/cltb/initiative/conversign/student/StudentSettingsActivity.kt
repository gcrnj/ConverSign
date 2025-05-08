package com.cltb.initiative.conversign.student

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.cltb.initiative.conversign.MainActivity
import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.databinding.ActivityStudentSettingsBinding
import com.cltb.initiative.conversign.game_data.GameFlow
import com.cltb.initiative.conversign.student.viewmodels.ProgressViewModel
import com.cltb.initiative.conversign.student.viewmodels.StudentProfileViewModel
import com.cltb.initiative.conversign.utils.CustomDialog
import com.cltb.initiative.conversign.utils.FirebaseStorageUtils
import com.cltb.initiative.conversign.utils.SharedPrefUtils
import com.google.firebase.auth.FirebaseAuth

class StudentSettingsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityStudentSettingsBinding.inflate(layoutInflater)
    }

    private val customDialog: CustomDialog by lazy {
        CustomDialog(this)
    }

    private val gameFlow = GameFlow()

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Image found
            // Add to firebase
            studentViewModel.uploadProfilePicture(uri = uri) { error ->
                error?.let {
                    // There is an error
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                } ?: run {
                    // Success
                    loadProfilePic()
                    Toast.makeText(this, "Profile Picture uploaded.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Profile Picture selection canceled.", Toast.LENGTH_SHORT).show()
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted
            // Pick profile picture
            pickImageLauncher.launch("image/*")
        } else if (shouldShowRequestPermissionRationale(getMediaPermissionType())) {
            // Denied once
            // Ask again using a dialog explaining why it is important
            customDialog.showDialog(
                title = "Permission required",
                message = "Please allow access to your photos",
                positiveText = "Allow",
                negativeText = "Cancel",
                positiveAction = {
                    // Request permission again
                    requestMediaPermission()
                }
            )
        } else {
            // Denied twice
            // Show an explanation to the user — maybe a dialog
            customDialog.showDialog(
                title = "Permission required",
                message = "Please allow access to your photos",
                positiveText = "Settings",
                negativeText = "Cancel",
                positiveAction = {
                    // Launch settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            )
        }
    }

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
        setupClickListeners()
        loadProfilePic()
    }

    private fun setupObservers() {
        studentViewModel.getStudent(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        progressViewModel.loadProgressFromFireStore(
            FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
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


    private fun setupClickListeners() = with(binding) {
        uploadProfileButton.setOnClickListener {
            requestMediaPermission()
        }

        editProfileButton.setOnClickListener {

        }

        logoutOption.setOnClickListener {
            SharedPrefUtils(this@StudentSettingsActivity).logout()
            startActivity(
                Intent(
                    this@StudentSettingsActivity,
                    MainActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            finish()
        }

    }


    private fun requestMediaPermission() {
        requestPermissionLauncher.launch(getMediaPermissionType())
    }

    private fun getMediaPermissionType(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun loadProfilePic() {
        val studentRef = FirebaseStorageUtils.studentProfileRef(
            FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
        studentRef.downloadUrl.addOnSuccessListener { uri ->
            binding.profileImageView.load(uri) {
                placeholder(R.drawable.profile_sample) // shown while loading
                error(R.drawable.profile_sample)         // shown if loading fails
            }
        }
    }

}