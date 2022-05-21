import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.LayoutRes
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.model.entities.Lecture
import javax.inject.Singleton

@Singleton
class AgendaWidgetListProvider(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val lectureList = mutableListOf<Lecture>()

    override fun onCreate() {}

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int = lectureList.count()

    override fun getViewAt(position: Int): RemoteViews {
        return constructRemoteViews(context, lectureList.get(position))
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
    fun addItem(lectureListElem: Lecture) {
        System.out.println("Agregado item a listado: "+lectureListElem)
        lectureList.add(lectureListElem)
    }
    fun clearLectureList(){
        lectureList.clear()
    }
    companion object {

        const val REQUEST_CODE_FROM_COLLECTION_WIDGET = 2
        const val EXTRA_VIEW_ID = "extra_view_id"
        const val REQUEST_CODE = "request_code"

        internal fun constructRemoteViews(
            context: Context,
            item: Lecture
        ): RemoteViews {
            System.out.println("Plasmando datos en AgendaWidgetListProvider")
            val remoteViews = RemoteViews(context.packageName, R.layout.agenda_widget_item)
            remoteViews.setTextViewText(R.id.widget_item_hora_comienzo, item.startDate.toString())
            remoteViews.setTextViewText(R.id.widget_item_hora_fin, item.endDate.toString())
            remoteViews.setTextViewText(R.id.widget_item_asignatura, item.subjectName)
            remoteViews.setTextViewText(R.id.widget_item_edificio, item.building.name)
            remoteViews.setTextViewText(R.id.widget_item_aula, item.lectureRoom.fullCode)
            return remoteViews
        }
    }
}