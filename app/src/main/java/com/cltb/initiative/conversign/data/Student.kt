package com.cltb.initiative.conversign.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Student(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val createdAt: Timestamp? = null,
    val phone: String = "",
    val classCode: String = "",
) : Parcelable {
    fun role() = Roles.Student.name

    fun fullName() = "$firstName $lastName"

    fun formattedCreatedAt(): String {
        createdAt ?: return ""
        val date = createdAt.toDate() // Convert to java.util.Date
        val format = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = format.format(date)
        return formattedDate
    }
}
