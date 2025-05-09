package com.cltb.initiative.conversign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Progress(
    val id: String = "",
    val milestone: Int,
    val level: Int,
    val section: Int,
    val timeTaken: Double = 0.0,
    val healthUsed: Int = 0,
    val isDone: Boolean = false,
) : Parcelable {

    fun isGreaterThan(other: Progress): Boolean {
        return compareValuesBy(this, other,
            { it.section },
            { it.level },
            { it.milestone }
        ) > 0
    }


}