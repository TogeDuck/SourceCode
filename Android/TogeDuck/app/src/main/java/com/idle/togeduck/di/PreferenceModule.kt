package com.idle.togeduck.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.idle.togeduck.common.TogeDuckColor
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_CAKE_COUNT
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_CHAT_ROOM_LIST
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_CHAT_ROOM_TALK_LIST
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_GUID
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_ACCESS_TOKEN_TOKEN
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_JWT_REFRESH_TOKEN
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_SELECTED_CELEBRITY
import com.idle.togeduck.di.PreferenceModule.PreferenceKeys.KEY_THEME
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.quest.talk.model.TalkRoom
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
const val CHAT_ROOM_LIST_NAME = "CHAT_ROOM_LIST"
const val CHAT_ROOM_TALK_LIST_NAME = "CHAT_ROOM_TALK_LIST"
const val CAKE_COUNT_NAME = "CAKE_COUNT_NAME"
const val THEME_NAME = "THEME_NAME"

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
            Log.d("로그", "PreferenceModule - getGuid() 호출됨 ${preferences[KEY_GUID]}")
            preferences[KEY_GUID]
        }

    suspend fun setGuid(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_GUID] = token
            Log.d("로그", "PreferenceModule - setGuid() 호출됨 ${preferences[KEY_GUID]}")
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

    val getChatRoomList = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            val gson = GsonBuilder().create()
            Log.d("로그", "PreferenceModule - getChatRoomList() 호출됨 ${preferences[KEY_CHAT_ROOM_LIST]}")
            gson.fromJson(preferences[KEY_CHAT_ROOM_LIST], mutableMapOf<Long, TalkRoom>().javaClass)
        }

    suspend fun setChatRoomList(chatRoomList: MutableMap<Long, TalkRoom>) {
        val gson = GsonBuilder().create()
        val json = gson.toJson(chatRoomList)

        dataStore.edit { preferences ->
            preferences[KEY_CHAT_ROOM_LIST] = json
        }
    }

    suspend fun removeChatRoomList() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_CHAT_ROOM_LIST)
        }
    }

    val getChatRoomTalkList = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            val gson = GsonBuilder().create()
            Log.d("로그", "PreferenceModule - getChatRoomTalkList() 호출됨 ${preferences[KEY_CHAT_ROOM_TALK_LIST]}")
            gson.fromJson(preferences[KEY_CHAT_ROOM_TALK_LIST], mutableMapOf<Long, MutableList<Talk>>().javaClass)
        }

    suspend fun setChatRoomTalkList(chatRoomList: MutableMap<Long, MutableList<Talk>>) {
        val gson = GsonBuilder().create()
        val json = gson.toJson(chatRoomList)

        dataStore.edit { preferences ->
            preferences[KEY_CHAT_ROOM_TALK_LIST] = json
        }
    }

    suspend fun removeChatRoomTalkList() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_CHAT_ROOM_TALK_LIST)
        }
    }

    val getCakeCount = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[KEY_CAKE_COUNT]
        }

    suspend fun setCakeCount(cakeCount: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_CAKE_COUNT] = cakeCount
        }
    }

    suspend fun removeCakeCount() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_CAKE_COUNT)
        }
    }

    val getTheme = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            val gson = GsonBuilder().create()
            gson.fromJson(preferences[KEY_THEME], TogeDuckColor::class.java)
        }

    suspend fun setTheme(theme: TogeDuckColor) {
        val gson = GsonBuilder().create()
        val json = gson.toJson(theme)

        dataStore.edit { preferences ->
            preferences[KEY_THEME] = json
        }
    }

    suspend fun removeTheme() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_THEME)
        }
    }

    private object PreferenceKeys {
        val KEY_JWT_REFRESH_TOKEN = stringPreferencesKey(JWT_REFRESH_TOKEN_NAME)
        val KEY_JWT_ACCESS_TOKEN_TOKEN = stringPreferencesKey(JWT_ACCESS_TOKEN_NAME)
        val KEY_GUID = stringPreferencesKey(GUID_NAME)
        val KEY_SELECTED_CELEBRITY = stringPreferencesKey(SELECTED_CELEBRITY_NAME)
        val KEY_CHAT_ROOM_LIST = stringPreferencesKey(CHAT_ROOM_LIST_NAME)
        val KEY_CHAT_ROOM_TALK_LIST = stringPreferencesKey(CHAT_ROOM_TALK_LIST_NAME)
        val KEY_CAKE_COUNT = intPreferencesKey(CAKE_COUNT_NAME)
        val KEY_THEME = stringPreferencesKey(THEME_NAME)
    }
}