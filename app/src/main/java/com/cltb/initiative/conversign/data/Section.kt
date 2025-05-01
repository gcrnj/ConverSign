package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.cltb.initiative.conversign.levels.Rookie
import kotlinx.parcelize.Parcelize

@Parcelize
open class Section(
    val name: String,
    val sectionNumber: Int,
    val levels: List<Level>,
): Parcelable


val sections: List<Section> = listOf(
    Rookie,
)
