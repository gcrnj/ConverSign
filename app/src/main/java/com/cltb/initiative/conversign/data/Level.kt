package com.cltb.initiative.conversign.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.cltb.initiative.conversign.levels.Alphabet
import kotlinx.parcelize.Parcelize

@Parcelize
open class Level(
    val name: String,
    val levelNumber: Int,
    @DrawableRes val icon: Int,
    val lessons: List<Lesson>,
) : Parcelable

/** @param lessonChallenges is also the lesson */
@Parcelize
data class Lesson(
    val name: String,
    val description: String,
    val lessonNumber: Int,
    val lessonChallenges: List<LessonChallenge>,
) : Parcelable

@Parcelize
data class LessonChallenge(
    val image: Int,
    val answer: String,
    val lessonHint: String = "",
    val challengeHint: String = "",
) : Parcelable

val rookieLevels: List<Level> = listOf(
    Alphabet,
)