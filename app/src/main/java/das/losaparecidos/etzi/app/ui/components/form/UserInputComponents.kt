package das.losaparecidos.etzi.app.ui.components.form

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.showDateRangePicker
import das.losaparecidos.etzi.app.utils.canBePassword
import das.losaparecidos.etzi.app.utils.format
import das.losaparecidos.etzi.app.utils.today
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


/**
 * Custom TextField that takes a boolean parameter indicating if the current value is valid.
 *
 * If the value is not valid the TextField appearance changes (red border) to reflect it
 * This field allows by default to not apply the validation before the user interacts for the first time with the TextField.
 * With the [ignoreFirstTime] parameter it's posible to disable this feature.
 *
 * @param value Field's current value.
 * @param onValueChange Callback for onValueChange event, it must take the new value as parameter.
 * @param isValid If the field's current value is valid. If false the field changes it's appearance to reflect this state.
 * @param ignoreFirstTime Apply [isValid] even if the user hasn't interacted with the field yet.
 */
@Composable
fun ValidatorTextField(
    value: String,
    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,

    isValid: Boolean = true,
    ignoreFirstTime: Boolean = false,

    label: @Composable () -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,

    singleLine: Boolean = true,
    maxLines: Int = 1,

    enabled: Boolean = true,
    readOnly: Boolean = false,

    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var isFirstTime: Boolean by rememberSaveable { mutableStateOf(true) }
    if (ignoreFirstTime) isFirstTime = false

    TextField(
        value = value,
        onValueChange = { isFirstTime = false; onValueChange(it) },

        isError = !(isFirstTime || isValid),

        modifier = modifier,

        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,

        singleLine = singleLine,
        maxLines = maxLines,

        enabled = enabled,
        readOnly = readOnly,

        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}


/**
 * Predefined and styled password field.
 *
 * It uses a [ValidatorTextField] and thus, it has the same capabilities.
 *
 * @param value Field's current value.
 * @param onValueChange Callback for onValueChange event, it must take the new value as parameter.
 * @param leadingIcon Socket for field's icon at the start.
 * @param isValid If the field's current value is valid. If false the field changes it's appearance to reflect this state.
 * @param ignoreFirstTime Apply [isValid] even if the user hasn't interacted with the field yet.
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,

    isValid: Boolean = true,
    ignoreFirstTime: Boolean = false,

    enabled: Boolean = true,

    label: @Composable (() -> Unit) = { Text(stringResource(R.string.password_label)) },
    placeholder: @Composable (() -> Unit)? = { Text(stringResource(R.string.password_placeholder)) },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Filled.VpnKey, contentDescription = stringResource(R.string.password_label)) },

    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    ValidatorTextField(
        value = value,
        onValueChange = { if (canBePassword(it)) onValueChange(it) }, // Update value only if the new value can be a valid password.

        modifier = modifier,
        enabled = enabled,

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

        label = label,
        placeholder = placeholder,
        isValid = isValid,
        ignoreFirstTime = ignoreFirstTime,

        leadingIcon = leadingIcon,

        // Trailing icon to show/hide password
        trailingIcon = {
            val description = if (passwordVisible) stringResource(R.string.password_icon_visible) else stringResource(R.string.password_icon_hidden)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                AnimatedVisibility(passwordVisible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(imageVector = Icons.Filled.Visibility, description, tint = MaterialTheme.colorScheme.tertiary)
                }
                AnimatedVisibility(!passwordVisible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(imageVector = Icons.Filled.VisibilityOff, description)
                }
            }
        },

        keyboardActions = keyboardActions,
    )
}


//------------------------------------------------

enum class DateRangeFieldTextMode { FROM_TO, TO, FROM }

@Composable
fun DateRangeDoubleField(
    modifier: Modifier = Modifier,

    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    dateRange: Pair<LocalDate, LocalDate> = Pair(LocalDate.today, LocalDate.today.plus(7, DateTimeUnit.DAY)),
    dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT),

    startDateLabel: @Composable () -> Unit = { Text(text = stringResource(id = R.string.date_from_label)) },
    startDatePlaceholder: @Composable (() -> Unit)? = { Text(text = dateFormatter.toString()) },
    startDateLeadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Today, contentDescription = stringResource(id = R.string.date_from_label)) },
    startDateTrailingIcon: @Composable (() -> Unit)? = null,

    endDateLabel: @Composable () -> Unit = { Text(text = stringResource(id = R.string.date_to_label)) },
    endDatePlaceholder: @Composable (() -> Unit)? = { Text(text = dateFormatter.toString()) },
    endDateLeadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Event, contentDescription = stringResource(id = R.string.date_to_label)) },
    endDateTrailingIcon: @Composable (() -> Unit)? = null,

    enabled: Boolean = true,
) {
    // Formatted date as string
    CenteredRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Start
        DateRangeField(
            modifier = Modifier.weight(1f),

            onDateRangeSelected = onDateRangeSelected,
            dateRange = dateRange,
            dateFormatter = dateFormatter,
            labelDisplayMode = DateRangeFieldTextMode.FROM,

            label = startDateLabel,
            placeholder = startDatePlaceholder,
            leadingIcon = startDateLeadingIcon,
            trailingIcon = startDateTrailingIcon,

            enabled = enabled,
        )

        // End
        DateRangeField(
            modifier = Modifier.weight(1f),

            onDateRangeSelected = onDateRangeSelected,
            dateRange = dateRange,
            dateFormatter = dateFormatter,
            labelDisplayMode = DateRangeFieldTextMode.TO,

            label = endDateLabel,
            placeholder = endDatePlaceholder,
            leadingIcon = endDateLeadingIcon,
            trailingIcon = endDateTrailingIcon,

            enabled = enabled,
        )
    }
}

@Composable
fun DateRangeField(
    modifier: Modifier = Modifier,

    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    dateRange: Pair<LocalDate, LocalDate> = Pair(LocalDate.today, LocalDate.today.plus(7, DateTimeUnit.DAY)),
    dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT),
    labelDisplayMode: DateRangeFieldTextMode = DateRangeFieldTextMode.FROM_TO,

    label: @Composable () -> Unit = { Text(text = stringResource(id = R.string.date_range)) },
    placeholder: @Composable (() -> Unit)? = { Text(text = dateFormatter.toString()) },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.DateRange, null) },
    trailingIcon: @Composable (() -> Unit)? = null,

    enabled: Boolean = true,
) {
    //---------------   Variables   ----------------//

    val context = LocalContext.current

    // Focus manager to open the dialog and remove focus once user finishes it's selection
    val focusManager = LocalFocusManager.current

    // Formatted date as string
    val dateRangeLabelText = when (labelDisplayMode) {
        DateRangeFieldTextMode.FROM_TO -> "${dateRange.first.format(dateFormatter)} - ${dateRange.second.format(dateFormatter)}"
        DateRangeFieldTextMode.FROM -> dateRange.first.format(dateFormatter)
        DateRangeFieldTextMode.TO -> dateRange.second.format(dateFormatter)
    }

    val onDateRangeSelected: (LocalDate, LocalDate) -> Unit = { startDate, endDate ->
        // Once the user finishes their selection clear the focus from this field
        focusManager.clearFocus()

        // Call event callback
        onDateRangeSelected(startDate, endDate)
    }

    val onDismiss = { focusManager.clearFocus() }

    Log.d("label", dateRangeLabelText)
    //-------------------   UI   -------------------//
    TextField(
        modifier = modifier.onFocusChanged {
            // If the field is focused open the picker dialog
            if (it.isFocused) {
                showDateRangePicker(context, initialStartDate = dateRange.first, initialEndDate = dateRange.second, onDateRangeSelected, onDismiss)
            }
        },

        label = label,
        leadingIcon = leadingIcon,
        placeholder = placeholder,

        value = dateRangeLabelText,
        onValueChange = {},

        trailingIcon = trailingIcon,

        readOnly = true,
        singleLine = true,
        maxLines = 1,

        enabled = enabled
    )
}