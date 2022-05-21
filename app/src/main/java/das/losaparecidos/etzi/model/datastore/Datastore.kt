package das.losaparecidos.etzi.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import das.losaparecidos.etzi.app.utils.CipherUtil
import das.losaparecidos.etzi.app.utils.NoCryptographicKeyException
import das.losaparecidos.etzi.model.entities.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/*************************************************
 **             Data Store Instance             **
 *************************************************/
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ETZI_DATASTORE")


/*************************************************
 **           Data Store Key Provider           **
 *************************************************/

// Singleton class used to get correct and predefined Data Store Keys.
@Suppress("FunctionName")
private object PreferencesKeys {
    val LAST_LOGGED_USER = stringPreferencesKey("last_logged_user")
    val REMEMBER_LOGIN = booleanPreferencesKey("remember_login")

    // Dynamic keys depending on the current user
    fun USER_LANG(user: String) = stringPreferencesKey("${user}_lang")
}


/*************************************************
 **         User Preferences Repository         **
 *************************************************/

/**
 * Class implementing a repository pattern. It provides access to app's settings and user preferences.
 *
 * It is annotated with Hilt's @Singleton so there's only one instance created on the application.
 * This class instance is created automatically by Hilt (Hilt also injects the required [Context])
 *
 * @property context Any context (required to get DataStore instance)
 */

@Singleton
class Datastore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cipher: CipherUtil,
) {

    companion object {
        private const val CIPHER_KEY = "last_logged_user"
    }


    /*------------------------------------------------
    |                 Login Settings                 |
    ------------------------------------------------*/

    //------------   Last Logged User   ------------//

    /**
     * Collects the first preference item generated by DataStores [Flow] and returns the last logged user.
     */

    suspend fun getLastLoggedUser(): AuthUser? {
        val encryptedData = context.dataStore.data.first()[PreferencesKeys.LAST_LOGGED_USER]

        return try {
            if (encryptedData != null) {
                val data = cipher.decryptData(CIPHER_KEY, encryptedData)
                Json.decodeFromString(data)
            } else null
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        } catch (e: NoCryptographicKeyException) {
            e.printStackTrace()
            null
        }
    }

    // Set the last logged user on DataStore Preferences
    suspend fun setLastLoggedUser(user: AuthUser) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LOGGED_USER] = cipher.encryptData(CIPHER_KEY, Json.encodeToString(user))
        }
    }


    //-------   Remember Login Preference   --------//

    suspend fun getRememberLogin(): Boolean = context.dataStore.data.first()[PreferencesKeys.REMEMBER_LOGIN] ?: false

    suspend fun setRememberLogin(rememberLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMEMBER_LOGIN] = rememberLogin
        }
    }


    /*------------------------------------------------
    |                User Preferences                |
    ------------------------------------------------*/
    /*
     * Current user's username is required in most these methods below.
     *
     * All getters returns a kotlin Flow in order to get automatic updates on the settings.
     * The Preferences flow is mapped to get only the desired preference or the default value if null.
     *
     * Getters are suspended to not block Main (UI) thread.
     */


    //----------   Language Preference   -----------//
    fun getUserLanguage(userLdap: String): Flow<String> =
        context.dataStore.data.map { it[PreferencesKeys.USER_LANG(userLdap)] ?: Locale.getDefault().language }

    suspend fun setUserLanguage(userLdap: String, langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_LANG(userLdap)] = langCode
        }
    }
}