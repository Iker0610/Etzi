package das.losaparecidos.etzi.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.SizeF
import android.widget.RemoteViews
import das.losaparecidos.etzi.R

/**
 * Implementation of App Widget functionality.
 */

class AgendaWidget : AppWidgetProvider() {
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
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
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
    val viewMapping: Map<SizeF, RemoteViews> = mapOf(
        // Specify the minimum width and height in dp and a layout, which you want to use for the
        // specified size
        // In the following case:
        //   - R.layout.widget_weather_forecast_small is used from
        //     180dp (or minResizeWidth) x 110dp (or minResizeHeight) to 269dp (next cutoff point - 1) x 279dp (next cutoff point - 1)
        //   - R.layout.widget_weather_forecast_medium is used from 270dp x 110dp to 270dp x 279dp (next cutoff point - 1)
        //   - R.layout.widget_weather_forecast_large is used from
        //     270dp x 280dp to 570dp (specified as maxResizeWidth) x 450dp (specified as maxResizeHeight)
        //Formula: (73n-16)(118m-16)
        //Y 70n-30?
        //2x3
        //MAXSizeF(516f, 213f) to RemoteViews(
        //SizeF(130f, 338f) to RemoteViews(
        //MINSizeF(118f,257f) to RemoteViews(
        SizeF(0f,0f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_2x3
        ),
        //2x4
        //SizeF(130f, 456f) to RemoteViews(
        //MAXSizeF(693f, 213f) to RemoteViews(
        //MINSizeF(118f,349f) to RemoteViews(
        //SizeF(117f,200f) to RemoteViews( //117x200 OFI1
        //SizeF(260f, 100f) to RemoteViews(
        SizeF(259f, 120f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_2x4
        ),
        //3x4
        //SizeF(203f, 456f) to RemoteViews(
        //MAXSizeF(693f, 327f) to RemoteViews(
        //MINSizeF(190f,349f) to RemoteViews(
        //SizeF(117f,256f) to RemoteViews(//OFI1
        //SizeF(118f, 340f) to RemoteViews(
        SizeF(259f, 220f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_3x4
        ),
        //4x4
        //SizeF(276f, 456f) to RemoteViews(
        //MAXSizeF(693f, 441f) to RemoteViews(
        //MINSizeF(253f,349f) to RemoteViews(
        //SizeF(180f,340f) to RemoteViews( //OFI1
        SizeF(259f, 327f) to RemoteViews(
            context.packageName,
            R.layout.agenda_widget_4x4
        )
    )

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, RemoteViews(viewMapping))
}