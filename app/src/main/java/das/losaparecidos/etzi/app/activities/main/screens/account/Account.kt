package das.losaparecidos.etzi.app.activities.main.screens.account

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                modifier = Modifier.statusBarsPadding(),
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