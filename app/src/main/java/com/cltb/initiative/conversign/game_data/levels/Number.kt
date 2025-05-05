package com.cltb.initiative.conversign.game_data.levels

import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Challenge
import com.cltb.initiative.conversign.data.ChallengeMilestone
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.data.LessonMilestone
import com.cltb.initiative.conversign.data.Level

object Number : Level(
    name = "Level 2: Numbers",
    levelNumber = 2,
    icon = R.drawable.number_1,
    milestones = listOf(
        // 1-2, 3-4, 5-6, 7-8, 9-10
        LessonMilestone(
            number = 1,
            roadMapTitle = "Lesson 1",
            pageHeader = "Lesson 1",
            pageSubHeader = "Numbers 1-2",
            lessons = listOf(
                Lesson(
                    signName = "1",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/1.png"
                ),
                Lesson(
                    signName = "2",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/2.png"
                ),
            )
        ),
        ChallengeMilestone(
            number = 2,
            roadMapTitle = "Challenge 1",
            pageHeader = "Challenge",
            challenges = listOf(
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "1"
                ),

                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "2"
                ),
            )
        ),
        LessonMilestone(
            number = 3,
            roadMapTitle = "Lesson 2",
            pageHeader = "Lesson 2",
            pageSubHeader = "Numbers 3-4",
            lessons = listOf(
                Lesson(
                    signName = "3",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/3.png"
                ),
                Lesson(
                    signName = "4",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/4.png"
                ),
            )
        ),
        ChallengeMilestone(
            number = 4,
            roadMapTitle = "Challenge 2",
            pageHeader = "Challenge",
            challenges = listOf(
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "3"
                ),
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "4"
                ),
            )
        ),
        LessonMilestone(
            number = 5,
            roadMapTitle = "Lesson 3",
            pageHeader = "Lesson 3",
            pageSubHeader = "Numbers 5-6",
            lessons = listOf(
                Lesson(
                    signName = "5",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/5.png"
                ),
                Lesson(
                    signName = "6",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/6.png"
                ),
            )
        ),
        ChallengeMilestone(
            number = 6,
            roadMapTitle = "Challenge 3",
            pageHeader = "Challenge",
            challenges = listOf(
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "5"
                ),
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "6"
                ),
            )
        ),
        LessonMilestone(
            number = 7,
            roadMapTitle = "Lesson 4",
            pageHeader = "Lesson 4",
            pageSubHeader = "Numbers 7-8",
            lessons = listOf(
                Lesson(
                    signName = "7",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/7.png"
                ),
                Lesson(
                    signName = "8",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/8.png"
                ),
            )
        ),
        ChallengeMilestone(
            number = 8,
            roadMapTitle = "Challenge 4",
            pageHeader = "Challenge",
            challenges = listOf(
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "7"
                ),
                Challenge(
                    hint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    answer = "8"
                ),
            )
        ),
        LessonMilestone(
            number = 9,
            roadMapTitle = "Lesson 5",
            pageHeader = "Lesson 5",
            pageSubHeader = "Numbers 9-10",
            lessons = listOf(
                Lesson(
                    signName = "9",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/9.png"
                ),
                Lesson(
                    signName = "10",
                    signHint = "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                    signFirebaseImage = "signs/numbers/10.png"
                ),
            )
        )
    )
)