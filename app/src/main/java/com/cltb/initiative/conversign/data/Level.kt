package com.cltb.initiative.conversign.data

import com.cltb.initiative.conversign.levels.Alphabet

open class Level(
    val name: String,
    val levelNumber: Int,
    val lessons: List<Lesson>,
)

data class Lesson(
    val name: String,
    val lessonNumber: Int,
    val challenges: List<Challenge>,
)

data class Challenge(
    val image: Int,
    val answer: String,
    val lessonHint: String = "",
    val challengeHint: String = "",
)

val levels = listOf(
    Alphabet,
    )