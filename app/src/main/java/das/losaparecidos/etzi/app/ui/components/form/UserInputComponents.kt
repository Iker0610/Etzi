package das.losaparecidos.etzi.app.ui.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.utils.canBePassword


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