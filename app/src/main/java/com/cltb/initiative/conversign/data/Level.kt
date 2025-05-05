package com.cltb.initiative.conversign.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.cltb.initiative.conversign.game_data.levels.Alphabet
import com.cltb.initiative.conversign.game_data.levels.Number
import kotlinx.parcelize.Parcelize

@Parcelize
open class Level(
    val name: String,
    val levelNumber: Int,
    @DrawableRes val icon: Int,
    val milestones: List<Milestone>,
) : Parcelable

abstract class Milestone: Parcelable{
    abstract val number: Int
    abstract val roadMapTitle: String
    abstract val pageHeader: String
}

@Parcelize
data class LessonMilestone(
    override val number: Int,
    override val roadMapTitle: String,
    override val pageHeader: String,
    val pageSubHeader: String, // e.g. "Lesson 1: Letters A-D"
    val lessons: List<Lesson>,
): Milestone()

@Parcelize
data class Lesson(
    val signName: String,
    val signHint: String,
    val signFirebaseImage: String,
) : Parcelable

@Parcelize
data class ChallengeMilestone(
    override val number: Int,
    override val roadMapTitle: String,
    override val pageHeader: String,
    val challenges: List<Challenge>,
): Milestone()

@Parcelize
data class Challenge(
    val hint: String = "",
    val answer: String = "", // e.g. Sign "B"
): Parcelable


val rookieLevels: List<Level> = listOf(
    Alphabet,
    Number,
)