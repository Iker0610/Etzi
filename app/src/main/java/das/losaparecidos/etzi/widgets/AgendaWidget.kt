package das.losaparecidos.etzi.widgets

import AgendaWidgetListProvider
import android.app.Application
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.SizeF
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivity
import das.losaparecidos.etzi.model.repositories.LoginRepository
import das.losaparecidos.etzi.model.repositories.StudentDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class AgendaWidget : AppWidgetProvider() {
    private val job= SupervisorJob()
    val coroutineScope=CoroutineScope(Dispatchers.IO + job)
    @Inject lateinit var repo: StudentDataRepository
    @Inject lateinit var loginRepo: LoginRepository


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val opciones = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val width = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val height = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val remoteViews = RemoteViews(context.packageName, getLayoutResponsive(width, height))
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        System.out.println("Se ha creado el widget")
    }

    override fun onDisabled(context: Context) {
        job.cancel()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, AgendaWidget::class.java))
        for(idWidget in ids){

            val opciones = appWidgetManager.getAppWidgetOptions(idWidget)
            val width = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val height = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val remoteViews = RemoteViews(context.packageName, getLayoutResponsive(width, height))


            coroutineScope.launch {
                if(loginRepo.getLastLoggedUser()!=null){
                    repo.getTodayTimetable().collect{ _itemHorario ->
                            if(_itemHorario.isEmpty()){
                                remoteViews.setViewVisibility(R.id.horario_agenda, View.GONE)
                                remoteViews.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                                remoteViews.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.VISIBLE)
                            }else{
                                remoteViews.setViewVisibility(R.id.horario_agenda, View.VISIBLE)
                                remoteViews.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                                remoteViews.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                            }

                            var i = Intent(context, WidgetService::class.java).apply {
                                //putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idWidget)
                                //data= Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                                val extrasBundle = Bundle()
                                
                            }
                            remoteViews.setRemoteAdapter(R.id.horario_agenda, i)
                            appWidgetManager.updateAppWidget(idWidget, remoteViews)
                        }
                }else{
                    remoteViews.setViewVisibility(R.id.horario_agenda, View.GONE)
                    remoteViews.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
                    remoteViews.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                    appWidgetManager.updateAppWidget(idWidget, remoteViews)
                }

            }
        }

    }

    private fun getLayoutResponsive(width: Int, height: Int): Int {
        var layout:Int
        if(width>270 && height>250){
            layout=R.layout.agenda_widget_4x4
        }else if (width>265 && height>118){
            layout = R.layout.agenda_widget_2x4
        }else{
            layout = R.layout.agenda_widget_2x3
        }
        return layout
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }
}