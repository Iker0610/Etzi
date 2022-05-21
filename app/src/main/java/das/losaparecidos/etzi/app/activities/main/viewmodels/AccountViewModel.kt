package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.AppLanguage
import das.losaparecidos.etzi.app.utils.LanguageManager
import das.losaparecidos.etzi.model.entities.Student
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val studentDataRepository: StudentDataRepository,
    private val languageManager: LanguageManager,
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
    //var studentLanguage: Flow<String> = studentDataRepository.getUserLanguage(currentUser)
    val currentSetLang by languageManager::currentLang
    val prefLang = studentDataRepository.getUserLanguage(currentUser).map { AppLanguage.getFromCode(it) }


    fun onLanguageChanged(lang: AppLanguage, context: Context) {
        // cambiar lenguaje en datastore y en la interfaz tb
        languageManager.changeLang(lang, context)
        viewModelScope.launch(Dispatchers.IO) {
            studentDataRepository.setUserLanguage(currentUser, lang.code)
        }
    }

    fun onPicChanged() {
        // subirla a firebase y reemplazar la actual en local
    }
    fun onLogout(){
        viewModelScope.launch (Dispatchers.IO){
            studentDataRepository.clearUserPreferences()
        }
    }
}