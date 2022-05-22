package das.losaparecidos.etzi.app.activities.main.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.AuthUser
import das.losaparecidos.etzi.model.repositories.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EgelaViewModel @Inject constructor(
    loginRepository: LoginRepository
): ViewModel(){
    var loggedUser: AuthUser = AuthUser("","")
    init{
        viewModelScope.launch(Dispatchers.IO) {
            loggedUser = loginRepository.getLastLoggedUser()!!
        }
    }
}