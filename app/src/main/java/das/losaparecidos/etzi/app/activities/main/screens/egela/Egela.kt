package das.losaparecidos.etzi.app.activities.main.screens.egela

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EgelaScreen(windowSizeClass: WindowWidthSizeClass) {
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    Scaffold (
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Egela.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = { scope.launch { navigationDrawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                })
        }
    ){  paddingValues ->
        val egela = rememberWebViewState("https://egela.ehu.eus/")

        WebView(
            egela,
            Modifier.padding(paddingValues)
        )
    }
}
@Composable
@Preview
fun EgelaScreenPreview(){
    EgelaScreen(WindowWidthSizeClass.Expanded)
}