import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.LayoutRes
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.Lecture
import javax.inject.Singleton


class AgendaWidgetListProvider(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val lectureList=mutableListOf<Lecture>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int = lectureList.count()

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.agenda_widget_item).apply{
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
    fun addItem(lectureListElem: Lecture) {
        lectureList.add(lectureListElem)
    }
}