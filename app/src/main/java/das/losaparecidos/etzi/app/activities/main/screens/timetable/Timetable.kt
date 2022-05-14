package das.losaparecidos.etzi.app.activities.main.screens.timetable

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen() {
    Scaffold { paddingValues ->

    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun TimetableScreenPreview() {
    EtziTheme {
        TimetableScreen()
    }
}