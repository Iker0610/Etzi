package das.losaparecidos.etzi.app.activities.main.screens

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PestControlRodent
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.RecentActors
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.Web
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.math.E


/**
 * Enum class that defines the different routes of the apk.
 *
 * @property route route for the navigation graph.
 * @property icon unique icon to be displayed that represents the route.
 */

/*
 * Splash Screen ✓
 * (Principal) Horario ✓
 * Tutorias ✓
 *   - (Principal) Tutorias ✓
 *   - (Secundaria) Alarmas de tutorias ✓
 * Expediente ✓
 *   - Asignaturas ✓
 *   - Creditos superados/restantes ✓
 *   - (Principal) Notas ✓
 *      * Nota media
 *      * Notas provisionales (si disponibles)
 *      * Asignaturas en curso
 * Preferencias / Perfil ✓
 * Acceso a egela ✓
 */
enum class MainActivityScreens(var route: String, var icon: ImageVector) {
    Splash("splash", Icons.Filled.Preview),
    Timetable("timetable", Icons.Filled.Watch),
    TutorialsSection("tutorials_section", Icons.Filled.SupervisedUserCircle),
    Tutorials("tutorials", Icons.Filled.SupervisedUserCircle),
    TutorialReminders("tutorial_reminders", Icons.Filled.SupervisedUserCircle),
    Record("record", Icons.Filled.Cases),
    Subjects("subjects",Icons.Filled.Book),
    Credits("credits", Icons.Filled.CardMembership),
    Grades("grades", Icons.Filled.Grade),
    Account("account", Icons.Filled.Person),
    Egela("egela", Icons.Filled.Web);


    // Get if this MainActivityScreen is one of the main screens
    fun isNavigable(): Boolean = this in mainScreens

    // Get the title of the screen from R.strings
    fun title(context: Context): String {
        val titleStringID = context.resources.getIdentifier("${this.route}_screen_title", "string", context.packageName)
        return context.getString(titleStringID)
    }

    // Utility variables and methods
    companion object {

        // List of screens that must appear in navigation rail / navigation drawer etc...
        val mainScreens = setOf(Timetable, Tutorials, Record, Egela)

        // Given a route get the corresponding MainActivityScreen
        // Original code from Google's Compose Navigation Codelab
        fun fromRoute(route: String?): MainActivityScreens =
            when (route?.substringBefore("/")) {
                Splash.route -> Timetable
                Timetable.route -> Timetable
                TutorialsSection.route -> TutorialsSection
                Tutorials.route -> Tutorials
                TutorialReminders.route -> TutorialReminders
                Record.route-> Record
                Subjects.route -> Subjects
                Credits.route -> Credits
                Grades.route -> Grades
                Account.route -> Account
                Egela.route -> Egela
                null -> Timetable
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }

        // Get if the given route is one of the main screens
        fun isNavigable(route: String?): Boolean = fromRoute(route).isNavigable()
    }
}