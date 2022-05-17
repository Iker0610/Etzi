package das.losaparecidos.etzi.app.ui.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.ZoneId


fun showDatePicker(context: Context, onDateSelected: (LocalDate) -> Unit) {
    val activity = context as AppCompatActivity

    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { dateMillis ->
            val selectedDate = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
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
            val firstSelectedDate = Instant.ofEpochMilli(selectionMillis.first).atZone(ZoneId.systemDefault()).toLocalDate()
            val secondSelectedDate = Instant.ofEpochMilli(selectionMillis.second).atZone(ZoneId.systemDefault()).toLocalDate()

            onDateSelected(firstSelectedDate, secondSelectedDate)
        }
    }
}