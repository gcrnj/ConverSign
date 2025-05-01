package com.cltb.initiative.conversign.student.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.utils.FireStoreUtils.progressCollectionRef
import com.google.gson.Gson

class ProgressViewModel : ViewModel() {

    private val _progress = MutableLiveData<Progress?>()
    val progress: LiveData<Progress?> get() = _progress

    fun setProgress(progress: Progress) {
        _progress.value = progress
    }

    fun loadProgressFromFireStore(userId: String) {
        progressCollectionRef(userId)
            .addSnapshotListener { doc, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching progress", error)
                    return@addSnapshotListener
                }
                doc?.firstOrNull()?.let {
                    val progress = try {
                        val json = Gson().toJson(it.data)
                        Gson().fromJson(json, Progress::class.java)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error parsing progress data", e)
                        null
                    }
                    _progress.value = progress
                }
            }
    }

    fun saveProgressToFireStore(userId: String) {
        _progress.value?.let { progress ->
            val data = Gson().toJson(progress)
            progressCollectionRef(userId)
                .add(data)
                .addOnSuccessListener {
                    Log.d("Firestore", "Progress saved successfully.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving progress", e)
                }
        }
    }
}
