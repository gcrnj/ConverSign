package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.cltb.initiative.conversign.game_data.sections.Master
import com.cltb.initiative.conversign.game_data.sections.Rookie
import com.cltb.initiative.conversign.game_data.sections.Veteran
import kotlinx.parcelize.Parcelize

@Parcelize
open class Section(
    val name: String,
    val sectionNumber: Int,
    val levels: List<Level>,
): Parcelable


val sections: List<Section> = listOf(
    Rookie,
    Veteran,
    Master,
)
