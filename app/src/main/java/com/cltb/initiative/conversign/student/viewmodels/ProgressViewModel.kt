package com.cltb.initiative.conversign.student.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.utils.FireStoreUtils.progressDoc
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await

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

    suspend fun saveNewProgressToFireStore(userId: String, newProgress: Progress?) {
        newProgress?.let { progress ->
            val json = Gson().toJson(progress)
            val data: Map<String, Any> =
                Gson().fromJson<Map<String, Any>?>(json, object : TypeToken<Map<String, Any>>() {}.type).apply {
                    toMutableMap().remove("health")
                    toMap()
                }
            progressDoc(userId)
                .update(data)
                .await()
        }
    }

    fun setNewProgress(userId: String) {
        val newProgress = Progress(
            currentMilestone = 1,
            currentLevel = 1,
            currentSection = 1,
            health = 3,
        )

        val progressJson = Gson().toJson(newProgress)
        val progressData: Map<String, Any> =
            Gson().fromJson(progressJson, object : TypeToken<Map<String, Any>>() {}.type)

        progressDoc(userId)
            .set(progressData, SetOptions.merge()) // this will add or update
            .addOnSuccessListener {
                _progress.value = newProgress
            }
            .addOnFailureListener { e ->
                _progress.value = null
            }
    }

    // Add 1 to current milestone
    fun nextMilestoneLevel(
        userId: String,
        section: Int = _progress.value?.currentSection ?: 0,
        level: Int = _progress.value?.currentLevel ?: 0,
        milestone: Int = _progress.value?.currentMilestone ?: 0,
        onSuccess: (Progress) -> Unit
    ) {
        val nextMilestone: Int?
        val nextLevel: Int?
        val nextSection: Int?
        with(_progress.value) {
            this ?: return

            val selectedSection = sections.find { it.sectionNumber == section }
            val selectedLevel = selectedSection?.levels?.find { it.levelNumber == level }
            val selectedMilestone = selectedLevel?.milestones?.find { it.number == milestone }

            if (selectedLevel?.milestones?.find { it.number == milestone + 1 } != null) {
                // there is a next milestone
                nextMilestone = milestone + 1
                nextLevel = level
                nextSection = section
            } else if (selectedSection?.levels?.find { it.levelNumber == level + 1 } != null) {
                // there is a next level
                // Reset milestone
                nextMilestone = 1
                nextLevel = level + 1
                nextSection = section
            } else if (sections.find { it.sectionNumber == section + 1 } != null) {
                // there is a next section
                // Reset milestone and level
                nextMilestone = 1
                nextLevel = 1
                nextSection = section + 1
            } else {
                // Finish
                nextMilestone = null
                nextLevel = null
                nextSection = null
            }
        }

        nextMilestone ?: return
        nextLevel ?: return
        nextSection ?: return

        val newProgress = Progress(
            currentMilestone = nextMilestone,
            currentLevel = nextLevel,
            currentSection = nextSection,
            health = 0
        )

        val progressJson = Gson().toJson(newProgress)
        val progressData: Map<String, Any> =
            Gson().fromJson(progressJson, object : TypeToken<Map<String, Any>>() {}.type)
        val mutableProgressData = progressData.toMutableMap()
        mutableProgressData.remove("health") // exclude health

        progressDoc(userId)
            .set(mutableProgressData, SetOptions.merge()) // this will add or update
            .addOnSuccessListener {
                _progress.value = newProgress
                onSuccess.invoke(newProgress)
            }
            .addOnFailureListener { e ->
                _progress.value = null
            }


    }

}
