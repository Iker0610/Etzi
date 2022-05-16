package das.losaparecidos.etzi.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.screens.MainActivityScreens
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.launch

// IMPORTANTE, EN EL EVENTO onNavigate hay que dar la ruta ( eso es el string )

@Composable
fun EtziNavigationBar(currentRoute: String?, onNavigate: (String) -> Unit) {

    val context = LocalContext.current

    NavigationBar {

        MainActivityScreens.mainSections.forEach { screen ->

            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title(context)) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) }
            )
        }
    }
}


@Composable
fun EtziNavigationRail(currentRoute: String?, onNavigate: (String) -> Unit, onMenuOpen: () -> Unit) {

    val context = LocalContext.current

    NavigationRail(
        header = {
            IconButton(
                onClick = onMenuOpen,
                content = { Icon(Icons.Rounded.Menu, null) }
            )
        }
    ) {

        CenteredColumn(
            Modifier
                .fillMaxHeight(1f)
                .padding(bottom = 64.dp)
        ) {

            MainActivityScreens.mainSections.forEach { screen ->

                if (currentRoute == screen.route) {
                    NavigationRailItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title(context)) },
                        selected = true,
                        onClick = { onNavigate(screen.route) }
                    )
                } else {
                    NavigationRailItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        selected = false,
                        onClick = { onNavigate(screen.route) }
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtziNavigationDrawer(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    drawerState: DrawerState,
    gesturesEnabled: Boolean = true,
    content: (@Composable () -> Unit) = {},
) {

    val context = LocalContext.current
    var itsFirst = true

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 32.dp)) {
                MainActivityScreens.menuScreens.forEach { (section, screens) ->

                    // Separador de secciones
                    if (itsFirst) itsFirst = false
                    else Divider(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 16.dp))

                    // Título de sección
                    Text(text = section.title(context), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp))

                    // Secciones
                    screens.forEach { screen ->

                        NavigationDrawerItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title(context)) },
                            selected = currentRoute == screen.route,
                            onClick = { onNavigate(screen.route) }
                        )
                    }
                }
            }
        },
        content = content
    )
}


//-----------------------------------------------------------------------------------------------------------------

// Previews
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EtziLogo(paddingValues: PaddingValues = PaddingValues(0.dp)) {
    CenteredBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 64.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ehu_logo),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = "Etzi",
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EtziNavigationBarPreview() {

    val (ventanaActual, setVentanaActual) = remember { mutableStateOf(MainActivityScreens.Tutorials.route) }

    EtziTheme {
        Scaffold(
            bottomBar = { EtziNavigationBar(ventanaActual, setVentanaActual) }
        ) { paddingValues -> EtziLogo(paddingValues) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EtziNavigationRailPreview() {

    val (ventanaActual, setVentanaActual) = remember { mutableStateOf(MainActivityScreens.Tutorials.route) }

    EtziTheme {
        Scaffold { paddingValues ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val scope = rememberCoroutineScope()

                EtziNavigationRail(ventanaActual, setVentanaActual, {})
                EtziLogo(paddingValues)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EtziNavigationDrawerPreview() {
    EtziTheme {
        val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val (ventanaActual, setVentanaActual) = remember { mutableStateOf(MainActivityScreens.Tutorials.route) }

        EtziNavigationDrawer(ventanaActual, setVentanaActual, navigationDrawerState, true) {
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = { Text("Prueba") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { navigationDrawerState.open() } }) {
                                Icon(Icons.Rounded.Menu, null)
                            }
                        }
                    )
                },
                bottomBar = { EtziNavigationBar(ventanaActual, setVentanaActual) }
            ) { paddingValues ->
                EtziLogo(paddingValues)
            }
        }
    }
}