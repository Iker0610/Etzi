package das.losaparecidos.etzi.widgets

import AgendaWidgetListProvider
import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return AgendaWidgetListProvider(applicationContext)
    }

}