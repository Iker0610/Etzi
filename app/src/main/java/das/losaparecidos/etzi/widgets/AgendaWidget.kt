package das.losaparecidos.etzi.widgets

import AgendaWidgetListProvider
import android.app.Application
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
            updateAppWidget(context, appWidgetManager, appWidgetId)
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
        val items = intent?.extras
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        //val opciones = appWidgetManager.getAppWidgetOptions(idWidget)
        //val width = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        //val height = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        //System.out.println("WIDTH: "+width+" HEIGHT: "+height)

        val remoteViews_2x3 = RemoteViews(context.packageName, R.layout.agenda_widget_2x3)
        val remoteViews_2x4 = RemoteViews(context.packageName, R.layout.agenda_widget_2x4)
        val remoteViews_4x4 = RemoteViews(context.packageName, R.layout.agenda_widget_4x4)
        System.out.println("onReceive")

        //val remoteViews = RemoteViews(context.packageName, getLayoutResponsive(width, height))
        //System.out.println(remoteViews)


        coroutineScope.launch {
            if(loginRepo.getLastLoggedUser()!=null && loginRepo.getRememberLogin()){
                repo.getTodayTimetable().collect{ _itemHorario ->

                    val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, AgendaWidget::class.java))


                    for(appWidgetId in ids){
                        val itr = _itemHorario.iterator()
                        val awlp = AgendaWidgetListProvider(context)
                        System.out.println("Tamaño lista: "+_itemHorario.size)
                        if(_itemHorario.isEmpty()){
                            remoteViews_2x3.setViewVisibility(R.id.horario_agenda, View.GONE)
                            remoteViews_2x3.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                            remoteViews_2x3.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.VISIBLE)
                            remoteViews_2x4.setViewVisibility(R.id.horario_agenda, View.GONE)
                            remoteViews_2x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                            remoteViews_2x4.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.VISIBLE)
                            remoteViews_4x4.setViewVisibility(R.id.horario_agenda, View.GONE)
                            remoteViews_4x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                            remoteViews_4x4.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.VISIBLE)
                        }else{
                            remoteViews_2x3.setViewVisibility(R.id.horario_agenda, View.VISIBLE)
                            remoteViews_2x3.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                            remoteViews_2x3.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                            remoteViews_2x4.setViewVisibility(R.id.horario_agenda, View.VISIBLE)
                            remoteViews_2x4.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                            remoteViews_2x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                            remoteViews_4x4.setViewVisibility(R.id.horario_agenda, View.VISIBLE)
                            remoteViews_4x4.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                            remoteViews_4x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                        }
                        while(itr.hasNext()){
                            val lectureListElem = itr.next()
                            awlp.addItem(lectureListElem)
                        }
                        remoteViews_2x3.setRemoteAdapter(
                            R.id.horario_agenda,
                            RemoteViews.RemoteCollectionItems.Builder().build()
                        )
                        remoteViews_2x4.setRemoteAdapter(
                            R.id.horario_agenda,
                            RemoteViews.RemoteCollectionItems.Builder().build()
                        )
                        remoteViews_4x4.setRemoteAdapter(
                            R.id.horario_agenda,
                            RemoteViews.RemoteCollectionItems.Builder().build()
                        )
                    }
                }
            }else{
                System.out.println("El usuario no ha iniciado sesión")
                remoteViews_2x3.setViewVisibility(R.id.horario_agenda, View.GONE)
                remoteViews_2x3.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
                remoteViews_2x4.setViewVisibility(R.id.horario_agenda, View.GONE)
                remoteViews_2x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
                remoteViews_4x4.setViewVisibility(R.id.horario_agenda, View.GONE)
                remoteViews_4x4.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
            }
        }
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, AgendaWidget::class.java))

        for(idWidget in ids){
            //updateAppWidget(context, appWidgetManager, idWidget)
            appWidgetManager.updateAppWidget(idWidget, RemoteViews(getMapaResponsive(context)))
        }

    }

    private fun getLayoutResponsive(width: Int, height: Int): Int {
        System.out.println("getLayoutResponsive")
        var layout:Int
        if(width>270 && height>250){
            layout=R.layout.agenda_widget_4x4
            System.out.println("Dimensión 4x4")
        }else if (width>265 && height>118){
            layout = R.layout.agenda_widget_2x4
            System.out.println("Dimensión 2x4")
        }else{
            layout = R.layout.agenda_widget_2x3
            System.out.println("Dimensión 2x3")
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
        /*val width = newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val height = newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        System.out.println("Width: "+width+". Height: "+height)*/
        //appWidgetManager?.updateAppWidget(appWidgetId, RemoteViews(getMapaResponsive(context)))

        //print("Width: "+newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH))
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    //val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    //val views = RemoteViews(context.packageName, R.layout.agenda_widget_3x4)
    //views.setTextViewText(R.id.appwidget_text, widgetText)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, RemoteViews(getMapaResponsive(context)))
}

fun getMapaResponsive(context: Context):Map<SizeF, RemoteViews>{
    val viewMapping: Map<SizeF, RemoteViews> = mapOf(
        SizeF(0f,0f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_2x3
        ),
        SizeF(265f, 120f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_2x4
        ),
        SizeF(270f, 354f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_4x4
        )
    )
    return viewMapping
}