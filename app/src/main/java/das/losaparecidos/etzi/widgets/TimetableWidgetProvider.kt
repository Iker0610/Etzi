package das.losaparecidos.etzi.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.Etzi
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.WidgetOpenerActions
import das.losaparecidos.etzi.app.activities.authentication.AuthenticationActivity
import das.losaparecidos.etzi.model.repositories.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimePeriod
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class TimetableWidgetProvider : AppWidgetProvider() {
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var loginRepository: LoginRepository


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("WIDGET", "Se han actualizado los widgets (onUpdate)")

        val theresLastLoggedUser = runBlocking { loginRepository.getLastLoggedUser() != null }

        // There may be multiple widgets active, so update all of them
        appWidgetIds.forEach { appWidgetId ->

            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val remoteView = RemoteViews(context.packageName, getLayoutResponsive(width, height))

            //Acciones específicas en función del RemoteView
            when (getLayoutResponsive(width,height)){
               R.layout.agenda_widget_4x4-> {
                   //Config fecha
                   val fecha = Date()
                   val sdfDia = SimpleDateFormat("dd")
                   val sdfMes = SimpleDateFormat("MMMM")
                   remoteView.setTextViewText(R.id.agenda_widget_fecha_dia, sdfDia.format(fecha))
                   remoteView.setTextViewText(R.id.agenda_widget_fecha_mes, sdfMes.format(fecha))
                   agregarEventoBotonesWidget(context, remoteView)
               }
                R.layout.agenda_widget_2x4->{
                    agregarEventoBotonesWidget(context, remoteView)
               }

            }

            // Mostramos la lista
            if (theresLastLoggedUser) {
                // Initialize intent for our widget service
                val widgetServiceIntent = Intent(context, TimetableWidgetService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                }

                // Get our responsive layout
                remoteView.apply {
                    setRemoteAdapter(R.id.horario_agenda, widgetServiceIntent)
                    setEmptyView(R.id.horario_agenda, R.id.agenda_widget_msg_no_clases)
                }

                // Update this widget data
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.horario_agenda)
            }
            // Mostramos el mensaje de login
            else{
                remoteView.apply {
                    setViewVisibility(R.id.horario_agenda, View.GONE)
                    setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
                    setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                }
            }

            // Actualizamos widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteView)
        }

        // Call super
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun agregarEventoBotonesWidget(context: Context, remoteView: RemoteViews) {
        val intentTutorias = Intent(context, AuthenticationActivity::class.java)
        intentTutorias.setAction(WidgetOpenerActions.OPEN_TUTORIALS.name)
        remoteView.setOnClickPendingIntent(R.id.btn_tutorias, PendingIntent.getActivity(context, WidgetOpenerActions.OPEN_TUTORIALS.hashCode(), intentTutorias, PendingIntent.FLAG_IMMUTABLE))
        val intentExpediente = Intent(context, AuthenticationActivity::class.java)
        intentExpediente.setAction(WidgetOpenerActions.OPEN_EXPEDIENTE.name)
        remoteView.setOnClickPendingIntent(R.id.btn_expediente, PendingIntent.getActivity(context, WidgetOpenerActions.OPEN_EXPEDIENTE.hashCode(), intentExpediente, PendingIntent.FLAG_IMMUTABLE))
        val intentEgela = Intent(context, AuthenticationActivity::class.java)
        intentEgela.setAction(WidgetOpenerActions.OPEN_EGELA.name)
        remoteView.setOnClickPendingIntent(R.id.btn_egela, PendingIntent.getActivity(context, WidgetOpenerActions.OPEN_EGELA.hashCode(), intentEgela, PendingIntent.FLAG_IMMUTABLE))
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("WIDGET", "Se ha habilitado el primer widget")
    }

    override fun onDisabled(context: Context) {
        job.cancel()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("WIDGET", "Se ha recibido un evento broadcast onReceive")

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, TimetableWidgetProvider::class.java))

        onUpdate(context, appWidgetManager, ids)


/*

        coroutineScope.launch {

            // New data
            //val timetable: Flow<List<Lecture>>? = loginRepository.getLastLoggedUser()//.let { dataRepository.getTodayTimetable() }


            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, TimetableWidgetProvider::class.java))


            ids.forEach { idWidget ->

                val opciones = appWidgetManager.getAppWidgetOptions(idWidget)
                val width = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
                val height = opciones.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
                val widgetMainLayout = RemoteViews(context.packageName, getLayoutResponsive(width, height))

                if (loginRepository.getLastLoggedUser() != null) {
                    *//*if (timetable != null) {
                        timetable.collect { lectures ->


                            if (lectures.isEmpty()) {
                                widgetMainLayout.setViewVisibility(R.id.horario_agenda, View.GONE)
                                widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                                widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.VISIBLE)
                            } else {
                                widgetMainLayout.setViewVisibility(R.id.horario_agenda, View.VISIBLE)
                                widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.GONE)
                                widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                            }*//*

*//*
                        val newIntent = Intent(context, TimetableWidgetService::class.java).apply {
                            //putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idWidget)
                            //data= Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                            val extrasBundle = Bundle()

                        }
                        widgetMainLayout.setRemoteAdapter(R.id.horario_agenda, newIntent)
                        appWidgetManager.updateAppWidget(idWidget, widgetMainLayout)*//*


                    // Set up the intent that starts the StackViewService, which will
                    // provide the views for this collection.
                    val updateIntent = Intent(context, TimetableWidgetService::class.java)*//*.apply {
                            // Add the widget ID to the intent extras.
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idWidget)
                            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                        }*//*

                    Log.d("WIDGET", "INTENT Y SET REMOTELAYOUT")

                    widgetMainLayout.setRemoteAdapter(R.id.horario_agenda, updateIntent)
                    //widgetMainLayout.setRemoteAdapter(R.id.horario_agenda, updateIntent)
                    appWidgetManager.notifyAppWidgetViewDataChanged(idWidget, R.id.horario_agenda)

                    // Instantiate the RemoteViews object for the widget layout.
                    *//*widgetMainLayout.apply {
                        // Set up the RemoteViews object to use a RemoteViews adapter.
                        // This adapter connects to a RemoteViewsService through the
                        // specified intent.
                        // This is how you populate the data.
                        setRemoteAdapter(R.id.horario_agenda, updateIntent)

                        // The empty view is displayed when the collection has no items.
                        // It should be in the same layout used to instantiate the
                        // RemoteViews object.
                        //setEmptyView(R.id.horario_agenda, R.id.agenda_widget_msg_no_clases)
                    }*//*
                    appWidgetManager.updateAppWidget(idWidget, widgetMainLayout)
                } else {
                    widgetMainLayout.setViewVisibility(R.id.horario_agenda, View.GONE)
                    widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_iniciar_sesion, View.VISIBLE)
                    widgetMainLayout.setViewVisibility(R.id.agenda_widget_msg_no_clases, View.GONE)
                    appWidgetManager.updateAppWidget(idWidget, widgetMainLayout)
                }
            }
        }
        */
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    companion object {

        fun getLayoutResponsive(width: Int, height: Int): Int = when {
            width > 270 && height > 250 -> R.layout.agenda_widget_4x4
            width > 265 && height > 118 -> R.layout.agenda_widget_2x4
            else -> R.layout.agenda_widget_2x3
        }
    }
}
