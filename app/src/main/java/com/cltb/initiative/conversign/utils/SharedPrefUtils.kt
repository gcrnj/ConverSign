package com.cltb.initiative.conversign.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPrefUtils(private val context: Context) {

    companion object {
        const val PREFERENCES_NAME = "converSignPreferences"
    }

    fun saveData(key: Keys, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key.name, value)
        editor.apply()  // or editor.commit() (commit is synchronous, apply is asynchronous)
    }

    fun getData(key: Keys): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        return sharedPreferences.getString(key.name, null)
    }

    enum class Keys {
        Role,
    }
}