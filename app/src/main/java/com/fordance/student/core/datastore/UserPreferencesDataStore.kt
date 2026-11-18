package com.fordance.student.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class CachedUserSession(
    val userId: String? = null,
    val email: String? = null,
    val username: String? = null,
    val accessToken: String? = null,
    val isLoggedIn: Boolean = false
)

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_USERNAME = stringPreferencesKey("username")
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val userSessionFlow: Flow<CachedUserSession> = dataStore.data.map { preferences ->
        CachedUserSession(
            userId = preferences[KEY_USER_ID],
            email = preferences[KEY_EMAIL],
            username = preferences[KEY_USERNAME],
            accessToken = preferences[KEY_ACCESS_TOKEN],
            isLoggedIn = preferences[KEY_IS_LOGGED_IN] ?: false
        )
    }

    suspend fun saveSession(
        userId: String,
        email: String,
        username: String,
        accessToken: String?
    ) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_EMAIL] = email
            preferences[KEY_USERNAME] = username
            if (accessToken != null) {
                preferences[KEY_ACCESS_TOKEN] = accessToken
            }
            preferences[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
