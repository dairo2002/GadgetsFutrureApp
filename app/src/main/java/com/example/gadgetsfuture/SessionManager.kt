package com.example.gadgetsfuture

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "MyAppPrefs",
        Context.MODE_PRIVATE
    )

    fun saveCredentials(username: String, password: String) {
        val editor = sharedPref.edit()
        editor.putString("USERNAME", username)
        editor.putString("PASSWORD", password)
        editor.apply()
    }

    fun getSavedUsername(): String? {
        return sharedPref.getString("admin@gmail.com", null)
    }

    fun getSavedPassword(): String? {
        return sharedPref.getString("admin", null)
    }

    fun clearCredentials() {
        val editor = sharedPref.edit()
        editor.remove("admin@gmail.com")
        editor.remove("admin")
        editor.apply()
    }
}
