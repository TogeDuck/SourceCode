package com.idle.togeduck.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_GUID
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_ACCESS_TOKEN_TOKEN
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_REFRESH_TOKEN
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_SELECTED_CELEBRITY
import com.idle.togeduck.favorite.model.Celebrity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val TOGEDUCK_PREFERENCE_DATASTORE_NAME = "TOGEDUCK_PREFERENCE_DATASTORE"
const val JWT_REFRESH_TOKEN_NAME = "JWT_REFRESH_TOKEN"
const val JWT_ACCESS_TOKEN_NAME = "JWT_ACCESS_TOKEN"
const val GUID_NAME = "GUID"
const val SELECTED_CELEBRITY_NAME = "SELECTED_CELEBRITY"

@Singleton
class PreferenceModule @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val getRefreshToken = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_JWT_REFRESH_TOKEN]
        }

    suspend fun setRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_JWT_REFRESH_TOKEN] = token
        }
    }

    suspend fun removeRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_JWT_REFRESH_TOKEN)
        }
    }

    val getAccessToken = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_JWT_ACCESS_TOKEN_TOKEN]
        }

    suspend fun setAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_JWT_ACCESS_TOKEN_TOKEN] = token
        }
    }

    suspend fun removeAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_JWT_ACCESS_TOKEN_TOKEN)
        }
    }

    val getGuid = dataStore.data
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

    val getSelectedCelebrity = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            val gson = GsonBuilder().create()
            gson.fromJson(preferences[KEY_SELECTED_CELEBRITY], Celebrity::class.java)
        }

    suspend fun setSelectedCelebrity(celebrity: Celebrity) {
        val gson = GsonBuilder().create()
        val json = gson.toJson(celebrity)

        dataStore.edit { preferences ->
            preferences[KEY_SELECTED_CELEBRITY] = json
        }
    }

    suspend fun removeSelectedCelebrity() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_SELECTED_CELEBRITY)
        }
    }

    private object PreferenceKeys {
        val KEY_JWT_REFRESH_TOKEN = stringPreferencesKey(JWT_REFRESH_TOKEN_NAME)
        val KEY_JWT_ACCESS_TOKEN_TOKEN = stringPreferencesKey(JWT_ACCESS_TOKEN_NAME)
        val KEY_GUID = stringPreferencesKey(GUID_NAME)
        val KEY_SELECTED_CELEBRITY = stringPreferencesKey(SELECTED_CELEBRITY_NAME)
    }
}