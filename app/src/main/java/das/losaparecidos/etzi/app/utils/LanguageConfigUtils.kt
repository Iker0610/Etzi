package das.losaparecidos.etzi.app.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.ComponentActivity
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


/**
 * Get a ComponentActivity from the context given if possible, otherwise returns null.
 */
private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


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
    ES("Español", "es");


    companion object {
        /**
         * Get the [AppLanguage] from a language code.
         *
         * @param code Language's code as string
         * @return That code's corresponding App's language as an [AppLanguage]. Defaults to [EN].
         */
        fun getFromCode(code: String) = when (code) {
            ES.code -> ES
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