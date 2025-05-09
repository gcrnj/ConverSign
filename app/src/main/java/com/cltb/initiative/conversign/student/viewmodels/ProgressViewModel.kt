package com.cltb.initiative.conversign.student.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.utils.FireStoreUtils.allProgressCollectionRef
import com.cltb.initiative.conversign.utils.FireStoreUtils.latestProgressDoc
import com.cltb.initiative.conversign.utils.FireStoreUtils.progressCollectionRef
import com.cltb.initiative.conversign.utils.toProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await

class ProgressViewModel : ViewModel() {

    private val _progress = MutableLiveData<Progress?>()
    val progress: LiveData<Progress?> get() = _progress

    fun loadProgressFromFireStore(userId: String) {
        latestProgressDoc(userId)
            .addSnapshotListener { doc, error ->
                if (error != null) {
                    Log.e("Firestore", "Error fetching progress", error)
                    return@addSnapshotListener
                }
                doc?.firstOrNull()?.let {
                    val progress = it.toProgress()
                    _progress.value = progress
                } ?: run {
                    setFirstProgress(userId)
                }
            }
    }

    suspend fun saveNewProgressToFireStore(
        userId: String,
        newProgress: Progress?,
        currentProgress: Progress,
        timeTaken: Double = 0.0,
        healthUsed: Int = 0,
    ) {
        newProgress?.let { progress ->
            val json = Gson().toJson(progress)
            val data: Map<String, Any> =
                Gson().fromJson<Map<String, Any>?>(
                    json,
                    object : TypeToken<Map<String, Any>>() {}.type
                ).apply {
                    toMap()
                }
            allProgressCollectionRef(userId)
                .add(data)
                .await()
        }

        // Update current progress
        progressCollectionRef(
            userId = userId,
            progressId = currentProgress.id,
        ).update(
            mapOf(
                "timeTaken" to timeTaken,
                "healthUsed" to healthUsed,
                "isDone" to true,
            )
        )
    }

    private fun setFirstProgress(userId: String) {
        val newProgress = Progress(
            milestone = 1,
            level = 1,
            section = 1,
        )

        val progressJson = Gson().toJson(newProgress)
        val progressData: Map<String, Any> =
            Gson().fromJson(progressJson, object : TypeToken<Map<String, Any>>() {}.type)

        allProgressCollectionRef(userId)
            .add(progressData) // this will add or update
            .addOnSuccessListener {
                loadProgressFromFireStore(userId)
            }
            .addOnFailureListener { e ->
                _progress.value = null
            }
    }

}
