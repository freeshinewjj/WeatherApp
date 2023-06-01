package com.jay.weatherapp.common.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jay.weatherapp.common.domain.model.weather.SearchParameter
import com.jay.weatherapp.common.domain.repositories.UserPrefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreUserPrefRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
): UserPrefRepository {
    override val lastSearchParameter: Flow<SearchParameter?>
        get() {
            return dataStore.data.catch { exp ->
                if (exp is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exp
                }
            }.map {
                val name = it[PrefKeys.LAST_SEARCH_PARAM_CITY]
                val state = it[PrefKeys.LAST_SEARCH_PARAM_STATE]
                val country = it[PrefKeys.LAST_SEARCH_PARAM_COUNTRY]
                if (name != null) {
                    SearchParameter(name, state, country)
                } else {
                    null
                }
            }
        }

    override suspend fun saveSearchParameter(searchParameter: SearchParameter) {
        dataStore.edit { preferences ->
            preferences[PrefKeys.LAST_SEARCH_PARAM_CITY] = searchParameter.city
            searchParameter?.state?.let {
                preferences[PrefKeys.LAST_SEARCH_PARAM_STATE] = it
            }
            searchParameter?.country?.let {
                preferences[PrefKeys.LAST_SEARCH_PARAM_COUNTRY] = it
            }
        }
    }

    object PrefKeys {
        val LAST_SEARCH_PARAM_CITY = stringPreferencesKey("LAST_SEARCH_PARAM_NAME")
        val LAST_SEARCH_PARAM_STATE = stringPreferencesKey("LAST_SEARCH_PARAM_STATE")
        val LAST_SEARCH_PARAM_COUNTRY = stringPreferencesKey("LAST_SEARCH_PARAM_COUNTRY")
    }
}