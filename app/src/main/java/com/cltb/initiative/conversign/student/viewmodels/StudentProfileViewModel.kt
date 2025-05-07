package com.cltb.initiative.conversign.student.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.FirebaseStorageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentProfileViewModel : ViewModel() {
    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> = _student

    fun getStudent(id: String = FirebaseAuth.getInstance().currentUser?.uid ?: "", fetchProgress: Boolean = false) {
        FireStoreUtils
            .studentCollectionRef(id)
            .addSnapshotListener { value, _ ->
                _student.value = value?.let {
                    val json = Gson().toJson(it.data)
                    val currentStudent = Gson().fromJson(json, Student::class.java)
                    if(fetchProgress) {
                        currentStudent.progress = fetchStudentProgress(id)
                    }
                    currentStudent
                }
            }
    }

    private fun fetchStudentProgress(id: String): Progress? {
        var progress: Progress? = null
        val latch = java.util.concurrent.CountDownLatch(1)

        FireStoreUtils
            .studentCollectionRef(id)
            .collection("progress")
            .get()
            .addOnSuccessListener { doc ->
                val document = doc.firstOrNull()
                if (document?.exists() == true) {
                    val json = Gson().toJson(document.data)
                    progress = Gson().fromJson(json, Progress::class.java)
                }
                latch.countDown()
            }
            .addOnFailureListener {
                latch.countDown()
            }

        latch.await() // blocks the current thread, be cautious if on UI thread
        return progress
    }


    fun uploadProfilePicture(
        uri: Uri,
        studentId: String = FirebaseAuth.getInstance().currentUser?.uid ?: "",
        onResult: (error: String?) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                // Validate the URI (you can add more checks if needed)
                if (uri.scheme != "content") {
                    onResult.invoke("Invalid URI scheme")
                    return@launch
                }

                // Upload to firebase
                val uploadTask = FirebaseStorageUtils.studentProfileRef(studentId = studentId).putFile(uri)
                uploadTask.await()

                onResult.invoke(null)
            } catch (e: Exception) {
                onResult.invoke(e.message ?: "There was an error uploading the profile picture.")
            }
        }
    }


}