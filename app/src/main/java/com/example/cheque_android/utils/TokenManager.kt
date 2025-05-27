package com.example.cheque_android.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object TokenManager {
    private const val PREF_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit { putString(TOKEN_KEY, token) }
    }

    fun getToken(context: Context): String? {
        return getPrefs(context).getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        getPrefs(context).edit { remove(TOKEN_KEY) }
    }

}
