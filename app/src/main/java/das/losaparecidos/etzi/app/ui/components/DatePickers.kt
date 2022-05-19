package das.losaparecidos.etzi.app.ui.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import das.losaparecidos.etzi.app.utils.epochUTCMilliseconds
import das.losaparecidos.etzi.app.utils.fromEpochMilliseconds
import das.losaparecidos.etzi.app.utils.today
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus


fun showDatePicker(
    context: Context,
    initialDate: LocalDate = LocalDate.today,
    onDateSelected: (LocalDate) -> Unit,
) {
    val activity = context as AppCompatActivity

    val picker = MaterialDatePicker.Builder.datePicker()
        .setSelection(initialDate.epochUTCMilliseconds)
        .build()

    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { dateMillis ->
            val selectedDate = LocalDate.fromEpochMilliseconds(dateMillis)
            onDateSelected(selectedDate)
        }
    }
}


fun showDateRangePicker(
    context: Context,
    initialStartDate: LocalDate = LocalDate.today,
    initialEndDate: LocalDate = LocalDate.today + DatePeriod(0, 0, 5),
    onDateSelected: (LocalDate, LocalDate) -> Unit,
) {
    val activity = context as AppCompatActivity

    val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair(
        initialStartDate.epochUTCMilliseconds,
        initialEndDate.epochUTCMilliseconds
    )).build()

    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { selectionMillis ->
            val firstSelectedDate = LocalDate.fromEpochMilliseconds(selectionMillis.first)
            val secondSelectedDate = LocalDate.fromEpochMilliseconds(selectionMillis.second)

            onDateSelected(firstSelectedDate, secondSelectedDate)
        }
    }
}




