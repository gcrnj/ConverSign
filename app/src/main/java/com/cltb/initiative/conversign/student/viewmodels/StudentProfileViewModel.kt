package com.cltb.initiative.conversign.student.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cltb.initiative.conversign.data.Student
import com.cltb.initiative.conversign.utils.FireStoreUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class StudentProfileViewModel : ViewModel() {
    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> = _student

    fun getStudent(id: String = FirebaseAuth.getInstance().currentUser?.uid ?: "'") {
        FireStoreUtils
            .studentCollectionRef(id)
            .addSnapshotListener { value, _ ->
                _student.value = value?.let {
                    val json = Gson().toJson(it.data)
                    Gson().fromJson(json, Student::class.java)
                }
            }
    }
}