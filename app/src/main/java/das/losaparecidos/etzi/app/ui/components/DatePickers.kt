package das.losaparecidos.etzi.app.ui.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun showDatePicker(context: Context, onDateSelected: (LocalDate) -> Unit) {
    val activity = context as AppCompatActivity

    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { dateMillis ->
            val selectedDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
            onDateSelected(selectedDate)
        }
    }
}


fun showDateRangePicker(context: Context, onDateSelected: (LocalDate, LocalDate) -> Unit) {
    val activity = context as AppCompatActivity

    val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair(
        MaterialDatePicker.todayInUtcMilliseconds(),
        MaterialDatePicker.todayInUtcMilliseconds() + (86400000 * 5) // 5 days
    )).build()

    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { selectionMillis ->
            val firstSelectedDate = Instant.fromEpochMilliseconds(selectionMillis.first).toLocalDateTime(TimeZone.currentSystemDefault()).date
            val secondSelectedDate = Instant.fromEpochMilliseconds(selectionMillis.second).toLocalDateTime(TimeZone.currentSystemDefault()).date

            onDateSelected(firstSelectedDate, secondSelectedDate)
        }
    }
}