package com.example.tictactoe.model_class

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

object DataStoreManager {

    private const val MERCHANT_DATASTORE = "ticTacToe_Datastore"
    const val BOX_COLOR_KEY = "Box color"
    const val X_MARK_KEY = "X mark color"
    const val O_MARK_KEY = "O mark color"

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = MERCHANT_DATASTORE)

    fun <T: Any> CoroutineScope.saveDataToStore(context: Context, key: String, value: T?) {
        this.launch(Dispatchers.Default) {
            context.dataStore.edit {
                when(value) {
                    is String -> it[stringPreferencesKey(key)] = (value as? String) ?: ""
                    is Int -> it[intPreferencesKey(key)] = (value as? Int) ?: 0
                    is Long -> it[longPreferencesKey(key)] = (value as? Long) ?: 0L
                    is Float -> it[floatPreferencesKey(key)] = (value as? Float) ?: 0f
                    is Double -> it[doublePreferencesKey(key)] = (value as? Double) ?: 0.0
                    is Set<*> -> it[stringSetPreferencesKey(key)] = value as? Set<String> ?: emptySet()
                }
            }
        }
    }

    inline fun <reified T> getDataFromStore(context: Context, key: String): Flow<T> {
        val simpleName = T::class.java.simpleName
        when {
            simpleName.equals("String", true) -> stringPreferencesKey(key)
            simpleName.equals("int", true) -> intPreferencesKey(key)
            simpleName.equals("long", true) -> longPreferencesKey(key)
            simpleName.equals("float", true) -> floatPreferencesKey(key)
            simpleName.equals("double", true) -> doublePreferencesKey(key)
            else -> stringSetPreferencesKey(key)
        }

        return context.dataStore.data.mapNotNull {
            it[stringPreferencesKey(key)]
        } as Flow<T>
    }

    suspend fun clearDataStore(context: Context) = context.dataStore.edit {
        it.clear()
    }
}
