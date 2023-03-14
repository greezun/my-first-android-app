package com.example.myapplication20.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_LOGIN = "USER_LOGIN"
private const val USER_PASSWORD ="USER_PASSWORD"
private const val AUTOLOGIN ="AUTOLOGIN"
private const val DATA_STORE_NAME = "userLogin"

class LoginData(context: Context) {

    private val dataStore = context.createDataStore(DATA_STORE_NAME)

    companion object {
        val USER_LOGIN_KEY = preferencesKey<String>(USER_LOGIN)
        val USER_PASSWORD_KEY = preferencesKey<String>(USER_PASSWORD)
        val AUTOLOGIN_KEY = preferencesKey<Boolean>(AUTOLOGIN)
    }

    suspend fun storeLoginData(login: String, password: String, isAutologin: Boolean) {
        dataStore.edit {
            it[USER_LOGIN_KEY] = login
            it[USER_PASSWORD_KEY] = password
            it[AUTOLOGIN_KEY] = isAutologin
        }
    }

    val loginFlow: Flow<String> = dataStore.data.map {
        it[USER_LOGIN_KEY] ?: ""
    }

    val passwordFlow: Flow<String> = dataStore.data.map {
        it[USER_PASSWORD_KEY] ?: ""
    }

    val autoLoginFlow: Flow<Boolean> = dataStore.data.map {
        it[AUTOLOGIN_KEY] ?: false
    }


}