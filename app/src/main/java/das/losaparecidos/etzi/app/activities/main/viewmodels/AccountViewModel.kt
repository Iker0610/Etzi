package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.Student
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val studentDataRepository: StudentDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    // Retrieve current logged user
    private val currentUser = (savedStateHandle.get("LOGGED_USER_LDAP") as? String)!!

    /*************************************************
     **                    States                   **
     *************************************************/
    var studentData: Flow<Student> = studentDataRepository.getStudentData()
    var studentLanguage: Flow<String> = studentDataRepository.getUserLanguage(currentUser)


    fun onLanguageChanged(langCode: String) {
        // cambiar lenguaje en datastore
        viewModelScope.launch(Dispatchers.IO) {
            studentDataRepository.setUserLanguage(currentUser, langCode)
        }
    }

    fun onPicChanged(lang: String) {
        // cambiar lenguaje en datastore
    }
}