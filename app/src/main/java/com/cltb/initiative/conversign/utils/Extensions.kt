package com.cltb.initiative.conversign.utils

import com.cltb.initiative.conversign.data.Educator
import com.cltb.initiative.conversign.data.Student
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson

fun DocumentSnapshot.toEducator(): Educator {
    val data = data?.toMutableMap() ?: mutableMapOf()
    data["id"] = id
    val json = Gson().toJson(data)
    return Gson().fromJson(json, Educator::class.java)
}

fun DocumentSnapshot.toStudent(): Student {
    val data = data?.toMutableMap() ?: mutableMapOf()
    data["id"] = id
    val json = Gson().toJson(data)
    return Gson().fromJson(json, Student::class.java)
}
