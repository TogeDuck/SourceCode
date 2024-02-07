package com.idle.togeduck.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_GUID
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_ACCESS_TOKEN_TOKEN
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_REFRESH_TOKEN
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val TOGEDUCK_PREFERENCE_DATASTORE_NAME = "TOGEDUCK_PREFERENCE_DATASTORE"
const val JWT_REFRESH_TOKEN_NAME = "JWT_REFRESH_TOKEN"
const val JWT_ACCESS_TOKEN_NAME = "JWT_ACCESS_TOKEN"
const val GUID_NAME = "GUID"

@Singleton
class PreferenceModule @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val jwtRefreshTokenFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_JWT_REFRESH_TOKEN]
        }

    suspend fun setJwtRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_JWT_REFRESH_TOKEN] = token
        }
    }

    suspend fun removeJwtRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_JWT_REFRESH_TOKEN)
        }
    }

    val jwtAccessTokenFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_JWT_ACCESS_TOKEN_TOKEN]
        }

    suspend fun setJwtAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_JWT_ACCESS_TOKEN_TOKEN] = token
        }
    }

    suspend fun removeJwtAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_JWT_ACCESS_TOKEN_TOKEN)
        }
    }

    val guidFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_GUID]
        }

    suspend fun setGuid(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_GUID] = token
        }
    }

    suspend fun removeGuid() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_GUID)
        }
    }

    private object PreferenceKeys {
        val KEY_JWT_REFRESH_TOKEN = stringPreferencesKey(JWT_REFRESH_TOKEN_NAME)
        val KEY_JWT_ACCESS_TOKEN_TOKEN = stringPreferencesKey(JWT_ACCESS_TOKEN_NAME)
        val KEY_GUID = stringPreferencesKey(GUID_NAME)
    }
}