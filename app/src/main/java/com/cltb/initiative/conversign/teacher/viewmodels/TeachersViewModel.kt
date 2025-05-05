package com.cltb.initiative.conversign.teacher.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext

class TeachersViewModel : ViewModel() {

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> = _students

    // Fetch students and their progress asynchronously
    fun fetchStudents(classCode: String) {
        // Use a coroutine to fetch data in the background
        viewModelScope.launch {
            val studentList = fetchStudentsWithProgress(classCode)
            _students.value = studentList
        }
    }

    // Suspend function to fetch students with progress
    private suspend fun fetchStudentsWithProgress(classCode: String): List<Student> {
        return withContext(Dispatchers.IO) {
            val studentList = mutableListOf<Student>()

            val snapshot = FireStoreUtils.allStudentsCollectionRef
                .whereEqualTo("classCode", classCode)
                .get()
                .await()  // Suspends the coroutine until Firestore returns the data

            snapshot.documents.forEach { document ->
                val json = Gson().toJson(document.data)
                val student = Gson().fromJson(json, Student::class.java)

                // Fetch the student's progress
                val progress = fetchStudentProgress(document.id)
                student.progress = progress

                studentList.add(student)
            }

            return@withContext studentList
        }
    }

    // Suspend function to fetch a student's progress asynchronously
    private suspend fun fetchStudentProgress(studentId: String): Progress? {
        return withContext(Dispatchers.IO) {
            val progressDoc = FireStoreUtils
                .studentCollectionRef(studentId)
                .collection("progress")
                .get()
                .await()  // Suspends the coroutine until Firestore returns the data

            val document = progressDoc.firstOrNull()
            if (document?.exists() == true) {
                val json = Gson().toJson(document.data)
                return@withContext Gson().fromJson(json, Progress::class.java)
            }

            return@withContext null
        }
    }
}
