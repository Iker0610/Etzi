package das.losaparecidos.etzi.app.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.launch
import kotlin.math.ln

// IMPORTANTE, EN EL EVENTO onNavigate hay que dar la ruta ( eso es el string )

@Composable
fun EtziNavigationBar(currentRoute: String?, onNavigate: (String) -> Unit) {

    val context = LocalContext.current

    NavigationBar {

        MainActivityScreens.mainSections.forEach { screen ->
            val selected by derivedStateOf { currentRoute == screen.route }
            val icon = if (selected) screen.selectedIcon else screen.unselectedIcon

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(screen.title(context)) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) }
            )
        }
    }

    /*----------------------------------------------------------------------------*/

    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val bottomNavBarColor = MaterialTheme.colorScheme
        .surfaceTint
        .copy(alpha = (((4.5f * ln(3.0.dp.value + 1)) + 2f) / 100f))
        .compositeOver(MaterialTheme.colorScheme.surface)

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setNavigationBarColor(
            color = bottomNavBarColor,
            darkIcons = useDarkIcons
        )
    }
}


@Composable
fun EtziNavigationRail(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onMenuOpen: () -> Unit
) {

    val context = LocalContext.current

    NavigationRail(
        header = {
            IconButton(
                onClick = onMenuOpen,
                content = { Icon(Icons.Rounded.Menu, null) }
            )
        }
    ) {

        CenteredColumn(Modifier.fillMaxHeight(1f)) {

            MainActivityScreens.mainSections.forEach { screen ->
                val selected by derivedStateOf { currentRoute == screen.route }
                val icon = if (selected) screen.selectedIcon else screen.unselectedIcon

                NavigationRailItem(
                    icon = { Icon(icon, contentDescription = null) },
                    label = { Text(screen.title(context)) },
                    selected = currentRoute == screen.route,
                    alwaysShowLabel = false,
                    onClick = { onNavigate(screen.route) },
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
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
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerContent = {

            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 32.dp)
            ) {
                MainActivityScreens.menuScreens.forEach { (section, screens) ->

                    // Título de sección
                    Text(
                        text = section.title(context),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 24.dp, bottom = 8.dp)
                    )

                    // Secciones
                    screens.forEach { screen ->
                        val selected by derivedStateOf { currentRoute == screen.route }
                        val icon = if (selected) screen.selectedIcon else screen.unselectedIcon

                        NavigationDrawerItem(
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(screen.title(context)) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                scope.launch {
                                    onNavigate(screen.route)
                                    drawerState.close()
                                }
                            }
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

                EtziNavigationRail(ventanaActual, setVentanaActual) {}
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