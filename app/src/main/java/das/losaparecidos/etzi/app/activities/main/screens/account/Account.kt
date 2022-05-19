package das.losaparecidos.etzi.app.activities.main.screens.account

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {
    val rememberNavController = rememberNavController()
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Account.title(LocalContext.current)) },
                navigationIcon = {
                    IconButton(onClick = { rememberNavController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }

                })
        }
    ) { paddingValues ->

    }
}

@Composable
@Preview
fun AccountScreenPreview() {
    EtziTheme() {
        AccountScreen()
    }
}