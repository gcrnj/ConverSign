package com.cltb.initiative.conversign.utils

import com.cltb.initiative.conversign.data.Roles
import com.google.firebase.storage.FirebaseStorage

object FirebaseStorageUtils {

    val profilePictures = FirebaseStorage.getInstance().getReference("profile_pictures")

    val allStudentsProfileRef = profilePictures.child(Roles.Student.name)
    fun studentProfileRef(studentId: String) = allStudentsProfileRef.child("$studentId.png")

    val allEducatorsProfileRef = profilePictures.child(Roles.Educator.name)
    fun educatorProfileRef(educatorId: String) = allEducatorsProfileRef.child(educatorId)

    val allSignsRef = profilePictures.child("signs")
    fun levelSignsRef(levelName: String) = allSignsRef.child(levelName)
}