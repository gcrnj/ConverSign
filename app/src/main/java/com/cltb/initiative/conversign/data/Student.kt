package com.cltb.initiative.conversign.data


data class Student(
    val name: String = "",
    val email: String = "",
    val age: Int = 0
) {
    val role = Roles.Student.name
}
