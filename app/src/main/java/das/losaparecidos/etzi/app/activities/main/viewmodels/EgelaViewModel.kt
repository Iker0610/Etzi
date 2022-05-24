package das.losaparecidos.etzi.app.activities.main.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.repositories.LoginRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EgelaViewModel @Inject constructor(
    loginRepository: LoginRepository
) : ViewModel() {
    var loggedUser: AuthUser = runBlocking { return@runBlocking loginRepository.getLastLoggedUser()!! }
}