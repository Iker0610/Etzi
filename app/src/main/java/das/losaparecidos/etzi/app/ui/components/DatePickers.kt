package das.losaparecidos.etzi.app.ui.components

import androidx.core.util.Pair
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


fun showDatePicker(activity: FragmentActivity, onDateSelected: (LocalDate) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()
    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { dateMillis ->
            val selectedDate = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            onDateSelected(selectedDate)
        }
    }
}


fun showDateRangePicker(activity: FragmentActivity, onDateSelected: (LocalDate, LocalDate) -> Unit) {
    val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair(
        MaterialDatePicker.todayInUtcMilliseconds(),
        MaterialDatePicker.todayInUtcMilliseconds() + (86400000 * 5)
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