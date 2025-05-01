package com.cltb.initiative.conversign.levels

import com.cltb.initiative.conversign.R
import com.cltb.initiative.conversign.data.Lesson
import com.cltb.initiative.conversign.data.LessonChallenge
import com.cltb.initiative.conversign.data.Level

object Alphabet : Level(
    name = "Level 1: Alphabets",
    levelNumber = 1,
    icon = R.drawable.letter_a,
    lessons = listOf(
        Lesson(
            name = "Lesson 1: Letters A-D",
            description = "Letters A-D",
            lessonNumber = 1,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "A",
                    "Close your hand into a fist with your thumb resting on the side of your index finger. The rest of the fingers are curled inward.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "B",
                    "Open your hand with fingers extended straight up, keeping your thumb across the palm to hold the fingers in place. Your palm faces outward.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "C",
                    "Curl your hand into a \"C\" shape, with your fingers forming a curved shape and your thumb resting on the outside.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "D",
                    "Hold up your right hand with the index finger pointing straight up, and the other fingers curled into the palm. Your thumb is placed across the palm.",
                ),
            )
        ),
        Lesson(
            name = "Lesson 2: Letters E-H",
            description = "Letters E-H",
            lessonNumber = 2,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "E",
                    "Curl your fingers into your palm, making a fist, but leave a small space between your thumb and your fingers, so the tips of your fingers touch your palm.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "F",
                    "Bring the tips of your thumb and index finger together to form a small \"O\" shape, while keeping your other three fingers extended upward.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "G",
                    "Extend your thumb and index finger straight out, keeping them close together, while the other fingers are curled into the palm.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "H",
                    "Hold your hand up with the index and middle fingers extended straight, while the other fingers are curled into the palm, and your thumb is resting against the side of your hand.",
                )
            ),
        ),
        Lesson(
            name = "Lesson 3: Letters I-L",
            description = "Letters I-L",
            lessonNumber = 3,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "I",
                    "Extend your pinky finger straight up, and curl the other fingers into a fist. Your thumb rests on the side of your hand.",
                ),

                LessonChallenge(
                    R.drawable.email_icon,
                    "J",
                    "Extend your pinky finger and trace the letter \"J\" in the air with it, while the other fingers are curled into a fist.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "K",
                    "Hold up your hand with your index and middle fingers extended straight, forming a \"V\" shape, and your thumb pointing upward, resting between the index and middle fingers.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "L",
                    "Hold your hand with the thumb extended out and your index finger extended straight up, forming an \"L\" shape.",
                ),
            ),
        ),
        Lesson(
            name = "Lesson 4: Letters M-P",
            description = "Letters M-P",
            lessonNumber = 4,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "M",
                    "Curl your thumb over the tips of your three middle fingers (index, middle, and ring), leaving your pinky extended.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "N",
                    "Similar to \"M,\" but place your thumb over your index and middle fingers, leaving the ring and pinky fingers extended.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "O",
                    "Form an \"O\" shape by bringing your thumb and fingers together, with your hand forming a small circle.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "P",
                    "Extend your index and middle fingers straight, and curl the other fingers into the palm. Point your thumb downward.",
                ),
            ),
        ),
        Lesson(
            name = "Lesson 5: Letters Q-U",
            description = "Letters Q-U",
            lessonNumber = 5,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "Q",
                    "Similar to \"G,\" but point your thumb and index finger downward, keeping them close together.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "R",
                    "Cross your index and middle fingers, keeping the other fingers curled into the palm, and your thumb resting against your palm.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "S",
                    "Make a fist, but keep your thumb resting over the top of your fingers, closing them tightly.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "T",
                    "Make a fist with your thumb tucked between your index and middle fingers.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "U",
                    "Extend your index and middle fingers straight up, while keeping the other fingers curled into the palm, and your thumb resting on the side of your hand.",
                ),
            ),
        ),
        Lesson(
            name = "Lesson 6: Letters V-Z",
            description = "Letters V-Z",
            lessonNumber = 6,
            lessonChallenges = listOf(
                LessonChallenge(
                    R.drawable.email_icon,
                    "V",
                    "Extend your index and middle fingers straight, forming a \"V\" shape, while keeping the other fingers curled into the palm.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "W",
                    " Extend your index, middle, and ring fingers straight, forming a \"W\" shape, while keeping the other fingers curled into the palm.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "X",
                    "Curl your index finger slightly, making a shape similar to a \"hook,\" while keeping the other fingers curled into a fist.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "Y",
                    "Extend your pinky and thumb out, while keeping the other fingers curled into the palm, forming a \"Y\" shape.",
                ),
                LessonChallenge(
                    R.drawable.email_icon,
                    "Z",
                    "Extend your index finger and draw the letter \"Z\" in the air.",
                ),
            ),
        )

    )
)