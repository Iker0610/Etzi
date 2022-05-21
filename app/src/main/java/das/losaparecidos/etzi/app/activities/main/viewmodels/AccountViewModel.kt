package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.app.utils.AppLanguage
import das.losaparecidos.etzi.app.utils.LanguageManager
import das.losaparecidos.etzi.model.entities.Student
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    var profilePicture: Bitmap? by mutableStateOf(null)
        private set

    var profilePicturePath: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            profilePicture = studentDataRepository.userProfileImage()
        }
    }
    fun onLanguageChanged(lang: AppLanguage, context: Context) {
        // cambiar lenguaje en datastore y en la interfaz tb
        languageManager.changeLang(lang, context)
        viewModelScope.launch(Dispatchers.IO) {
            studentDataRepository.setUserLanguage(currentUser, lang.code)
        }
    }

    fun onLogout(){
        viewModelScope.launch (Dispatchers.IO){
            studentDataRepository.clearUserPreferences()
        }
    }
    //-------------   Profile Related   ------------//
    private fun setProfileImage(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            profilePicture = null
            profilePicture = studentDataRepository.setUserProfileImage(image)
        }
    }

    fun setProfileImage() {
        viewModelScope.launch(Dispatchers.IO) {
            val image = BitmapFactory.decodeFile(profilePicturePath!!)
            setProfileImage(image)
        }
    }
    // This method does not reload the interface, just adjust the locale
    fun reloadLang(lang: AppLanguage, context: Context) = languageManager.changeLang(lang, context, false)
}