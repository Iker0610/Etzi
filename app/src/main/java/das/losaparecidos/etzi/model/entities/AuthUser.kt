package das.losaparecidos.etzi.model.entities

import kotlinx.serialization.Serializable


/*******************************************************************************
 ****                        User Entity in Database                        ****
 *******************************************************************************/

/**
 * Data class representing the user entity. Defined by a [ldap] and a [password].
 */
@Serializable
data class AuthUser(
    val ldap: String,
    val password: String = "",
)