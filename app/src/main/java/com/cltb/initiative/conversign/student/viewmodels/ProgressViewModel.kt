package com.cltb.initiative.conversign.student.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.utils.FireStoreUtils.progressDoc
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProgressViewModel : ViewModel() {

    private val _progress = MutableLiveData<Progress?>()
    val progress: LiveData<Progress?> get() = _progress

    fun loadProgressFromFireStore(userId: String) {
        progressDoc(userId)
            .addSnapshotListener { doc, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching progress", error)
                    return@addSnapshotListener
                }
                doc?.data?.let {
                    val progress = try {
                        val json = Gson().toJson(it)
                        Gson().fromJson(json, Progress::class.java)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error parsing progress data", e)
                        null
                    }
                    _progress.value = progress
                } ?: run {
                    _progress.value = null
                }
            }
    }

    fun saveProgressToFireStore(userId: String) {
        _progress.value?.let { progress ->
            val json = Gson().toJson(progress)
            val data: Map<String, Any> = Gson().fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
            progressDoc(userId)
                .update(data)
                .addOnSuccessListener {
                    Log.d("Firestore", "Progress saved successfully.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving progress", e)
                }
        }
    }

    fun setNewProgress(userId: String) {
        val newProgress = Progress(
            currentLesson = 1,
            currentChallenge = 0,
            currentLevel = 1,
            currentSection = 1,
            health = 3,
        )

        val progressJson = Gson().toJson(newProgress)
        val progressData: Map<String, Any> = Gson().fromJson(progressJson, object : TypeToken<Map<String, Any>>() {}.type)

        progressDoc(userId)
            .set(progressData, SetOptions.merge()) // this will add or update
            .addOnSuccessListener {
                _progress.value = newProgress
            }
            .addOnFailureListener { e ->
                _progress.value = null
            }
    }

}
