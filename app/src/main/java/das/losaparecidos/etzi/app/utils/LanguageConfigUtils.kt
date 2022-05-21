package das.losaparecidos.etzi.app.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.ListItem
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/*******************************************************************************
 ****                             Language Utils                            ****
 *******************************************************************************/

/**
 * Set of utils required for custom language management.
 *
 * Google does not support custom language (Locale) settings, and the solution is quite "hacky".
 */

/*************************************************
 **          App's Available Languages          **
 *************************************************/

/**
 * Class containing the App's available languages.
 *
 * @property language Full name of that language (in that language)
 * @property code Language's locale code
 */
enum class AppLanguage(val language: String, val code: String) {
    EN("English", "en"),
    ES("Español", "es"),
    EU("Basque", "eu");


    companion object {
        /**
         * Get the [AppLanguage] from a language code.
         *
         * @param code Language's code as string
         * @return That code's corresponding App's language as an [AppLanguage]. Defaults to [EN].
         */
        fun getFromCode(code: String) = when (code) {
            ES.code -> ES
            EU.code -> EU
            else -> EN
        }
    }
}


/*************************************************
 **            App's Language Manager           **
 *************************************************/

/**
 * Class to manage the current app's language.
 *
 * It is annotated with Hilt's singleton annotation so only one instance is created in the whole Application.
 */
@Singleton
class LanguageManager @Inject constructor() {

    // Current application's lang
    var currentLang: AppLanguage = AppLanguage.getFromCode(Locale.getDefault().language.lowercase())

    // Method to change the App's language setting a new locale
    fun changeLang(lang: AppLanguage, context: Context, recreate: Boolean = true) {

        // Check if there's any difference in language variables
        if (lang != currentLang || currentLang.code != Locale.getDefault().language) {

            // With the context create a new Locale and update configuration
            context.resources.apply {
                val locale = Locale(lang.code)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)

                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, displayMetrics)
            }

            // Update current language
            currentLang = lang

            // If asked recreate the interface (this does not finish the activity)
            if (recreate) context.getActivity()?.recreate()
        }
    }
}
/*************************************************
 **         App's Language Picker Dialog        **
 *************************************************/

/**
 * Custom dialog with a scrollable list in middle that allows the user to pick one of the available languages.
 *
 * It follows Material Design's Confirmation Dialog design pattern, as stated in:
 * https://material.io/components/dialogs#confirmation-dialog
 *
 * @param title Dialog title.
 * @param selectedLanguage Current selected language.
 * @param onLanguageSelected Callback for onLanguageSelected event.
 * @param onDismiss Callback for dismiss event.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerDialog(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit,
) {
    /*------------------------------------------------
    |                     States                     |
    ------------------------------------------------*/

    var selected by remember { mutableStateOf(selectedLanguage.code) }


    /*------------------------------------------------
    |                 User Interface                 |
    ------------------------------------------------*/
    AlertDialog(
        onDismissRequest = onDismiss,

        icon = { Icon(Icons.Rounded.Language, null) },
        title = { Text(text = stringResource(id = R.string.select_lang_dialog_title), textAlign = TextAlign.Center) },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.select_lang_dialog_text), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                Divider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.secondary)

                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {
                    AppLanguage.values().forEach { lang ->
                        ListItem(
                            modifier = Modifier.clickable { selected = lang.code },
                            trailing = { Checkbox(checked = selected == lang.code, onCheckedChange = { selected = lang.code }) },
                            text = { Text(text = lang.language) }
                        )
                    }
                }

                Divider(Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.secondary)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.cancel_button)) } },
        confirmButton = { TextButton(onClick = { onLanguageSelected(AppLanguage.getFromCode(selected)) }) { Text(text = stringResource(R.string.ok_button)) } }
    )
}