package das.losaparecidos.etzi.app.activities.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.Student
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val studentDataRepository: StudentDataRepository): ViewModel(){
    init {
        Log.d("VIEWMODEL", "Se ha creado un ${this::class}")
    }

    /*************************************************
     **                    States                   **
     *************************************************/
    var studentData: Flow<Student> = emptyFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            studentData = studentDataRepository.getStudentData()
            // get image
            // get selected language (if any)
        }
    }
}