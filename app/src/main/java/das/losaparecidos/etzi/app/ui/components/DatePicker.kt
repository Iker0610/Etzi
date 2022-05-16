package das.losaparecidos.etzi.app.ui.components

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.datepicker.RangeDateSelector
import das.losaparecidos.etzi.R
import java.time.LocalDate

/**
 * A Jetpack Compose compatible Date Picker adapted with the updated time library LocalDate from Java.
 * @author Arnau Mora, Joao Gavazzi, Aitor Javier Perez
 * @param minDate The minimum date allowed to be picked.
 * @param maxDate The maximum date allowed to be picked.
 * @param onDateSelected Will get called when a date gets picked.
 * @param onDismissRequest Will get called when the user requests to close the dialog.
 */
@Composable
fun DatePicker(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                ).verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.select_date_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.toString(),// .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(
                minDate,
                maxDate,
                onDateSelected = {
                    selDate.value = it
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.textButtonColors(),
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel_button),
                    )
                }

                Button(
                    onClick = {
                        val newDate = selDate.value
                        onDateSelected(
                            // This makes sure date is not out of range
                            LocalDate.of(newDate.year, newDate.month, newDate.dayOfMonth)
                        )
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(),
                ) {
                    //TODO - hardcode string
                    Text(
                        text = stringResource(id = R.string.ok_button),
                    )
                }

            }
        }
    }
}


/**
 * Used at [DatePicker] to create the calendar picker.
 * @author Arnau Mora, Joao Gavazzi
 * @param minDate The minimum date allowed to be picked.
 * @param maxDate The maximum date allowed to be picked.
 * @param onDateSelected Will get called when a date is selected.
 */
@Composable
private fun CustomCalendarView(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateSelected: (LocalDate) -> Unit
) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalendarViewCustom))
        }
    ) { view ->
        if (minDate != null)
            view.minDate = minDate
        if (maxDate != null)
            view.maxDate = maxDate

        view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onDateSelected(
                LocalDate.of(year, month + 1, dayOfMonth)
            )
        }
    }
}

/********************************* RANGE DATE PICKER ****************************/
@Composable
fun RangeDatePicker(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateFromSelected: (LocalDate) -> Unit,
    onDateToSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val selFromDate = remember { mutableStateOf(LocalDate.now()) }
    val selToDate = remember { mutableStateOf(LocalDate.now()) }
}

@Composable
private fun CustomRangeCalendarView(
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateFromSelected: (LocalDate) -> Unit,
    onDateToSelected: (LocalDate) -> Unit
) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalendarViewCustom))
        }
    ) { view ->
        if (minDate != null)
            view.minDate = minDate
        if (maxDate != null)
            view.maxDate = maxDate

        view.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onDateFromSelected(
                LocalDate.of(year, month + 1, dayOfMonth)
            )
            onDateToSelected(
                LocalDate.of(year, month + 1, dayOfMonth)
            )
        }
    }
}
/**
 * Con qué código llamar al composable Datepicker
var showPicker by rememberSaveable { mutableStateOf(false) }
if (showPicker)
DatePicker(onDateSelected = {
Log.i("fecha", it.toString())
},
onDismissRequest = {
showPicker = false
})
Button(onClick = { showPicker = true }, modifier = Modifier.padding(paddingValues)) {
Text(text = "Date picker")
}
 * */