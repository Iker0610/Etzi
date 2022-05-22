package das.losaparecidos.etzi.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class TimetableWidgetService : RemoteViewsService() {

    @Inject
    lateinit var dataRepository: StudentDataRepository

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        Log.d("WIDGET-SERVICE", "onGetViewFactory")
        return TimetableWidgetFactory(applicationContext, intent, dataRepository)
    }
}


class TimetableWidgetFactory(
    private val context: Context,
    intent: Intent,
    private var dataRepository: StudentDataRepository
) : RemoteViewsService.RemoteViewsFactory {

    private val mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    private val lectureList = mutableListOf<Lecture>()


    override fun onCreate() {
        Log.d("WIDGET-FACTORY", "onCreate")
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = lectureList.count()

    override fun onDataSetChanged() {
        Log.d("WIDGET-FACTORY", "onDataSetChanged")
        runBlocking {
            dataRepository.getTodayTimetable().collect { lecture ->
                lectureList.addAll(lecture)
            }
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d("WIDGET-FACTORY", "Generando UN item")
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