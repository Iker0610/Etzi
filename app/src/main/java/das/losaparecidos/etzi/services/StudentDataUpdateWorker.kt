package das.losaparecidos.etzi.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import das.losaparecidos.etzi.model.repositories.LoginRepository
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

private const val MAX_NUMBER_OF_RETRY = 5

@HiltWorker
class TimeTableWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val loginRepository: LoginRepository,
    private val studentDataRepository: StudentDataRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (!logUser()) {
            return@withContext if (runAttemptCount < MAX_NUMBER_OF_RETRY) Result.retry() else Result.failure()
        } else {
            return@withContext try {

                // Launch updates asynchronously
                val updateTimetableProcess = async { studentDataRepository.overwriteTimetable() }
                val updateStudentDataProcess = async { studentDataRepository.updateStudentData() }

                // Wait for both to end
                updateStudentDataProcess.await()
                updateTimetableProcess.await()

                // Return success
                Result.success()

            } catch (e: Exception) {
                e.printStackTrace()
                if (runAttemptCount < MAX_NUMBER_OF_RETRY) Result.retry() else Result.failure()
            }
        }
    }

    private suspend fun logUser() = loginRepository.getLastLoggedUser()?.let { lastLoggedUser -> loginRepository.authenticateUser(lastLoggedUser) } ?: false
}