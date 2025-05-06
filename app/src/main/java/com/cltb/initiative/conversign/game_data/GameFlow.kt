package com.cltb.initiative.conversign.game_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cltb.initiative.conversign.data.ChallengeMilestone
import com.cltb.initiative.conversign.data.LessonMilestone
import com.cltb.initiative.conversign.data.Level
import com.cltb.initiative.conversign.data.Milestone
import com.cltb.initiative.conversign.data.Progress
import com.cltb.initiative.conversign.data.Section
import com.cltb.initiative.conversign.data.sections
import com.cltb.initiative.conversign.student.StudentsActivity
import com.cltb.initiative.conversign.student.fragments.ChallengeFragment
import com.cltb.initiative.conversign.student.fragments.LessonFragment

class GameFlow(
    private val fragment: Fragment,
    private val studentsActivity: StudentsActivity,
) {


    fun getSection(
        currentSection: Int,
    ): Section? {
        val section = sections.find { it.sectionNumber == currentSection }
        return section
    }

    fun getLevel(
        currentSection: Int,
        currentLevel: Int,
    ): Level? {
        val selectedLevel =
            getSection(currentSection)?.levels?.find { it.levelNumber == currentLevel }
        return selectedLevel
    }

    fun getMilestone(
        currentSection: Int,
        currentLevel: Int,
        currentMilestone: Int,
    ): Milestone? {
        val selectedLevel = getLevel(currentSection, currentLevel)
        val selectedMilestone = selectedLevel?.milestones?.find { it.number == currentMilestone }
        return selectedMilestone
    }

    /***
     * Navigate to the next milestone page
     */
    suspend fun next(
        firebaseProgress: Progress,
        currentSection: Int,
        currentLevel: Int,
        currentMilestone: Int,
        onProgressUpdated: suspend (Progress) -> Unit = {},
    ) {
        val selectedSection = sections.find { it.sectionNumber == currentSection }
        val selectedLevel = selectedSection?.levels?.find { it.levelNumber == currentLevel }
        val selectedMilestone = selectedLevel?.milestones?.find { it.number == currentMilestone }

        selectedSection ?: return
        selectedLevel ?: return
        selectedMilestone ?: return

        val nextProgress: Progress
        // Check if last milestone
        if (selectedMilestone == selectedLevel.milestones.last()) {
            // Last milestone
            // Check if last level too
            if (selectedLevel == selectedSection.levels.last()) {
                // Last level
                // Check if last section
                if (selectedSection == sections.last()) {
                    // Last section
                    // Finish game
                    return
                } else {
                    // Not last section
                    // Go to the next section
                    nextProgress = firebaseProgress.copy(
                        currentSection = currentSection + 1,
                        currentLevel = 1, // Reset level
                        currentMilestone = 1 // Reset milestone
                    )
                }
            } else {
                // Not last level
                // Go to the next level
                nextProgress = firebaseProgress.copy(
                    currentSection = currentSection,
                    currentLevel = currentLevel + 1,
                    currentMilestone = 1 // Reset milestone
                )
            }
        } else {
            // Not last milestone
            // Go to the next milestone
            nextProgress = firebaseProgress.copy(
                currentSection = currentSection,
                currentLevel = currentLevel,
                currentMilestone = currentMilestone + 1
            )
        }

        if(nextProgress.isGreaterThan(firebaseProgress)) {
            // Update firebase to a greater value
            onProgressUpdated(nextProgress)
        }

        val nextMilestone = getMilestone(
            currentSection = nextProgress.currentSection,
            currentLevel = nextProgress.currentLevel,
            currentMilestone = nextProgress.currentMilestone,
        )
        changeFragment(
            nextMilestone,
            nextProgress.currentSection,
            nextProgress.currentLevel,
            nextProgress.currentMilestone,
        )

    }

    /**
     * Just navigate to the previous milestone and page
     * @param currentMilestone The current milestone number.
     * `currentMilestone` shouldn't be 1.
     * */
    fun previous(
        currentSection: Int,
        currentLevel: Int,
        currentMilestone: Int,
    ) {
        // Do not proceed if current milestone is 0
        if (currentMilestone < 1) return

        val previousMilestone = getMilestone(
            currentSection = currentSection,
            currentLevel = currentLevel,
            currentMilestone = currentMilestone - 1,
        )

        // Go to the previous fragment
        changeFragment(
            milestone = previousMilestone,
            currentSection = currentSection,
            currentLevel = currentLevel,
            currentMilestone = currentMilestone - 1,
        )

    }

    fun changeFragment(
        milestone: Milestone?,
        currentSection: Int,
        currentLevel: Int,
        currentMilestone: Int,
        popCurrentFragment: Boolean = true,
    ) {
        val args = Bundle().apply {
            putInt(SECTION_NUMBER, currentSection)
            putInt(LEVEL_NUMBER, currentLevel)
            putInt(MILESTONE_NUMBER, currentMilestone)
        }
        when (milestone) {
            is LessonMilestone -> {
                // Go to the previous which is a lesson
                studentsActivity.changeFragment(
                    fragmentClass = LessonFragment::class.java,
                    args = args,
                    popCurrentFragment = popCurrentFragment,
                )
            }

            is ChallengeMilestone -> {
                studentsActivity.changeFragment(
                    fragmentClass = ChallengeFragment::class.java,
                    args = args,
                    popCurrentFragment = popCurrentFragment,
                )
            }
        }
    }




    companion object {
        const val SECTION_NUMBER = "SECTION_NUMBER"
        const val LEVEL_NUMBER = "LEVEL_NUMBER"
        const val MILESTONE_NUMBER = "MILESTONE_NUMBER"
    }
}