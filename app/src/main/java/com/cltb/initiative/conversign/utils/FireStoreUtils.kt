package com.cltb.initiative.conversign.utils

import com.cltb.initiative.conversign.data.Roles
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreUtils {
    // Student
    val allStudentsCollectionRef = FirebaseFirestore.getInstance().collection(Roles.Student.name)
    fun studentCollectionRef(userId: String) = allStudentsCollectionRef.document(userId)
    fun currentStudentCollectionRef() = studentCollectionRef(FirebaseAuth.getInstance().currentUser?.uid ?: "")

    // Educator
    val allEducatorsCollectionRef = FirebaseFirestore.getInstance().collection(Roles.Educator.name)
    fun educatorCollectionRef(userId: String) = allEducatorsCollectionRef.document(userId)
    fun currentEducatorCollectionRef() = educatorCollectionRef(FirebaseAuth.getInstance().currentUser?.uid ?: "")

    // Individual
    val allIndividualsCollectionRef = FirebaseFirestore.getInstance().collection(Roles.Individual.name)
    fun individualCollectionRef(userId: String) = allIndividualsCollectionRef.document(userId)
    fun currentIndividualCollectionRef() = individualCollectionRef(FirebaseAuth.getInstance().currentUser?.uid ?: "")

    // General user by role
    fun allUsersCollectionRef(role: String) = FirebaseFirestore.getInstance().collection(role)
    fun userCollectionRef(role:String, userId: String) = allUsersCollectionRef(role).document(userId)
    fun currentUserCollectionRef(role: String) = userCollectionRef(role, FirebaseAuth.getInstance().currentUser?.uid ?: "")

    // Levels
    fun progressCollectionRef(userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: "") = studentCollectionRef(userId).collection("progress")
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    object StudentKeys {
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val CLASS_CODE = "classCode"
        const val CREATED_AT = "created_at"
    }
}