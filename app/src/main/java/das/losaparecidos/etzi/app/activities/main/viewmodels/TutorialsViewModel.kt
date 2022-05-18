package das.losaparecidos.etzi.app.activities.main.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.losaparecidos.etzi.model.entities.SubjectTutorial
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TutorialsViewModel @Inject constructor(
    private val studentDataRepository: StudentDataRepository
) : ViewModel() {

    private var allTutorials: List<SubjectTutorial> = emptyList()


    var tutorials: List<SubjectTutorial> by mutableStateOf(emptyList())
        private set



    init {
        viewModelScope.launch {
            allTutorials = studentDataRepository.getTutorials()
        }
    }


}