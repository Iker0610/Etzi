package das.losaparecidos.etzi.model.entities

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


/*******************************************************************************
 ****                        User Entity in Database                        ****
 *******************************************************************************/

/**
 * Data class representing the user entity. Defined by a [ldap] and a [password].
 */
@Immutable
@Serializable
data class AuthUser(
    val ldap: String,
    val password: String = "",
)