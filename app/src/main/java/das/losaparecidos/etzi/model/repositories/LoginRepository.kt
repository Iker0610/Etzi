package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.datastore.Datastore
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.webclients.AuthenticationClient
import das.losaparecidos.etzi.model.webclients.AuthenticationException
import javax.inject.Inject

interface ILoginRepository {
    suspend fun getLastLoggedUser(): AuthUser?
    suspend fun getRememberLogin(): Boolean
    suspend fun setLastLoggedUser(user: AuthUser, rememberLogin: Boolean)

    suspend fun authenticateUser(authUser: AuthUser): Boolean
}


class LoginRepository @Inject constructor(
    private val authenticationClient: AuthenticationClient,
    private val datastore: Datastore,
) : ILoginRepository {
    override suspend fun getLastLoggedUser(): AuthUser? = datastore.getLastLoggedUser()
    override suspend fun getRememberLogin(): Boolean = datastore.getRememberLogin()


    override suspend fun setLastLoggedUser(user: AuthUser, rememberLogin: Boolean) {
        datastore.setLastLoggedUser(user)
        datastore.setRememberLogin(rememberLogin)
    }


    @Throws(Exception::class)
    override suspend fun authenticateUser(authUser: AuthUser): Boolean {
        return try {
            authenticationClient.authenticate(authUser)
            true
        } catch (e: AuthenticationException) {
            false
        }
    }
}