package das.losaparecidos.etzi.widgets

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.entities.LectureEntity
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class TimetableWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        System.out.println("onGetViewFactory")
        return TimetableWidgetFactory(applicationContext)
    }
}


class TimetableWidgetFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    @Inject
    lateinit var dataRepository: StudentDataRepository
    val lectureList= mutableListOf<Lecture>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        System.out.println("onCreate")
    }

    override fun onDataSetChanged() {
        System.out.println("onDataSetChanged")
        coroutineScope.launch {
            dataRepository.getTodayTimetable().collect{lecture->
                lectureList.addAll(lecture)

            }
        }

    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun getCount(): Int = lectureList.count()

    override fun getViewAt(position: Int): RemoteViews {
        System.out.println("Plasmando datos")
        return RemoteViews(context.packageName, R.layout.agenda_widget_item).apply {
            setTextViewText(R.id.widget_item_hora_comienzo, lectureList[position].startDate.toString())
            setTextViewText(R.id.widget_item_hora_fin, lectureList[position].endDate.toString())
            setTextViewText(R.id.widget_item_asignatura, lectureList[position].subjectName)
            setTextViewText(R.id.widget_item_edificio, lectureList[position].building.name)
            setTextViewText(R.id.widget_item_aula, lectureList[position].lectureRoom.fullCode)
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}