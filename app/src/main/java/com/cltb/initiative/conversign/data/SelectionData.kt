package com.cltb.initiative.conversign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionData(
    val documentId: String,
    val id: Int,
    val name: String,
    val type: String, // Level or Lesson
) : Parcelable
