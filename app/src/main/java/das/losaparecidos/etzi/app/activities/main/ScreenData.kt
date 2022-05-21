package das.losaparecidos.etzi.app.activities.main

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


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
 *   - (Secundaria) Recordatorios de tutorias ✓
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
    Timetable("timetable", Icons.Filled.CalendarToday),
    TutorialsSection("tutorials_section", Icons.Filled.SupervisedUserCircle),
    Tutorials("tutorials", Icons.Filled.SupervisedUserCircle),
    TutorialReminders("tutorial_reminders", Icons.Filled.SupervisedUserCircle),
    Record("record", Icons.Filled.FileOpen),
    Subjects("subjects",Icons.Filled.Book),
    Credits("credits", Icons.Filled.CardMembership),
    Grades("grades", Icons.Filled.Grade),
    Exams("exams", Icons.Filled.EventNote),
    Account("account", Icons.Filled.Person),
    Egela("egela", Icons.Filled.Web);


    // Get if this MainActivityScreen is one of the main screens
    fun hasNavigationElements(): Boolean = this in screensWithNavigationElements

    // Get the title of the screen from R.strings
    fun title(context: Context): String {
        val titleStringID = context.resources.getIdentifier("${this.route}_screen_title", "string", context.packageName)
        return context.getString(titleStringID)
    }

    // Utility variables and methods
    companion object {

        val screensWithNavigationElements = setOf(Timetable, Tutorials, TutorialReminders, Grades, Credits, Subjects, Exams, Egela)

        // List of screens that must appear in navigation rail / navigation bar
        val mainSections = setOf(Timetable, TutorialsSection, Record, Egela)

        // List of screens that must appear in navigation drawer
        val menuScreens = mapOf(
            Timetable to setOf(Timetable),
            TutorialsSection to setOf(Tutorials, TutorialReminders),
            Record to setOf(Subjects, Grades, Credits, Exams),
            Egela to setOf(Egela)
        )

        val screenRouteToSectionRouteMapping = mapOf(
            Timetable.route to Timetable.route,
            Tutorials.route to TutorialsSection.route,
            TutorialReminders.route to TutorialsSection.route,
            Grades.route to Record.route,
            Subjects.route to Record.route,
            Credits.route to Record.route,
            Exams.route to Record.route,
            Egela.route to Egela.route,
            Account.route to Account.route
        )

        // Given a route get the corresponding MainActivityScreen
        // Original code from Google's Compose Navigation Codelab
        private fun fromRoute(route: String?): MainActivityScreens =
            when (route?.substringBefore("/")) {
                Timetable.route -> Timetable
                TutorialsSection.route -> TutorialsSection
                Tutorials.route -> Tutorials
                TutorialReminders.route -> TutorialReminders
                Record.route-> Record
                Subjects.route -> Subjects
                Credits.route -> Credits
                Grades.route -> Grades
                Exams.route -> Exams
                Account.route -> Account
                Egela.route -> Egela
                else -> Timetable
            }

        // Get if the given route is one of the main screens
        fun hasNavigationElements(route: String?): Boolean = fromRoute(route).hasNavigationElements()
    }
}