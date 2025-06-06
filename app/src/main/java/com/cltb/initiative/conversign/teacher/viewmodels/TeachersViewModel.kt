package com.cltb.initiative.conversign.teacher.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cltb.initiative.conversign.data.Educator
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.cltb.initiative.conversign.utils.toEducator
import com.cltb.initiative.conversign.utils.toProgress
import com.cltb.initiative.conversign.utils.toStudent
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TeachersViewModel : ViewModel() {

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> = _students

    private val _educator = MutableLiveData<Educator>()
    val educator: LiveData<Educator> = _educator

    fun fetchTeacherData(userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: "") {
        FireStoreUtils.educatorCollectionRef(userId)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val educator = it.toEducator()
                    _educator.value = educator
                }
            }
    }

    fun fetchStudentsWithLevel(classCode: String) {
        viewModelScope.launch {
            val studentList = fetchStudentsProgress(
                classCode = classCode,
            )
            _students.value = studentList
        }
    }


    // Suspend function to fetch students with progress
    private suspend fun fetchStudentsProgress(
        classCode: String,
    ): List<Student> {
        return withContext(Dispatchers.IO) {
            val studentList = mutableListOf<Student>()

            val query = FireStoreUtils.allStudentsCollectionRef
                .whereEqualTo("classCode", classCode)


            val snapshot =
                query.get().await()  // Suspends the coroutine until Firestore returns the data

            snapshot.documents.forEach { document ->
                val student = document.toStudent()

                // Fetch the student's progress
                val progress = fetchStudentProgress(document.id)
                student.progress = progress

                studentList.add(student)
            }

            return@withContext studentList
        }
    }

    // Suspend function to fetch a student's progress asynchronously
    private suspend fun fetchStudentProgress(
        studentId: String,
    ): Progress? {
        return withContext(Dispatchers.IO) {
            val progressDoc = FireStoreUtils
                .latestProgressDoc(studentId)
                .get()
                .await()  // Suspends the coroutine until Firestore returns the data

            val document = progressDoc.firstOrNull()
            if (document?.exists() == true) {
                return@withContext document.toProgress()
            }

            return@withContext null
        }
    }
}
