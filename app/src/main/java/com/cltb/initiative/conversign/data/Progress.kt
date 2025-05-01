package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.type.DateTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class Progress(
    val currentLesson: Int,
    val currentLevel: Int,
    val currentSection: Int,
    val lastAttempt: Timestamp,
    val score: Int,
) : Parcelable {

}