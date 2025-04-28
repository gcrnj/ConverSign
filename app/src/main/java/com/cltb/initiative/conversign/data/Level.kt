package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.cltb.initiative.conversign.levels.Alphabet
import kotlinx.parcelize.Parcelize

@Parcelize
open class Level(
    val name: String,
    val levelNumber: Int,
    val lessons: List<Lesson>,
): Parcelable

@Parcelize
data class Lesson(
    val name: String,
    val lessonNumber: Int,
    val challenges: List<Challenge>,
): Parcelable

@Parcelize
data class Challenge(
    val image: Int,
    val answer: String,
    val lessonHint: String = "",
    val challengeHint: String = "",
): Parcelable

val levels = listOf(
    Alphabet,
    )