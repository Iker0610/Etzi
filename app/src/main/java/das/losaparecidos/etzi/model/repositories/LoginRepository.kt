package das.losaparecidos.etzi.model.repositories

import das.losaparecidos.etzi.model.entities.AuthUser

interface ILoginRepository {
    suspend fun getLastLoggedUser(): AuthUser?
    suspend fun setLastLoggedUser(user: AuthUser)

    suspend fun authenticateUser(authUser: AuthUser): Boolean
}