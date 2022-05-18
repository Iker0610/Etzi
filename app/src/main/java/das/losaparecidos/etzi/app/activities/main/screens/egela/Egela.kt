package das.losaparecidos.etzi.app.activities.main.screens.egela

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EgelaScreen(windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit) {

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Egela.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ) { paddingValues ->
        val egela = rememberWebViewState("https://egela.ehu.eus/")

        WebView(
            egela,
            Modifier.padding(paddingValues)
        )
    }
}