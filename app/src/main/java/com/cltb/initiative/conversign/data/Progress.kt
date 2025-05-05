package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Progress(
    val currentMilestone: Int,
    val currentLevel: Int,
    val currentSection: Int,
    val lastAttempt: Timestamp? = null,
    val health: Int,
) : Parcelable