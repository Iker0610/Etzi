package das.losaparecidos.etzi.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import kotlinx.coroutines.launch

// IMPORTANTE, EN EL EVENTO onNavigate hay que dar la ruta ( eso es el string )

@Composable
fun EtziNavigationBar(onNavigate: (String) -> Unit) {

    TODO("Emplear la Navigation Bar")
    /*
     * Links relevantes:
     * - https://m3.material.io/components/navigation-bar/overview
     * - https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationbar
     */


    /*
    MainActivityScreens.mainScreens.forEach {screen ->
           TODO("Crear botones del tipo adecuado con los datos de la pantalla screen")
           //  https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationbaritem
    }
       */
}


@Composable
fun EtziNavigationRail(onNavigate: (String) -> Unit) {
    TODO("Emplear la Navigation Rail")
    /*
     * Links relevantes:
     * - https://m3.material.io/components/navigation-rail/overview
     * - https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationrail
     *
     * Bottones: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationrailitem
     */
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtziNavigationDrawer(
    drawerState: DrawerState,
    onNavigate: (String) -> Unit,
    gesturesEnabled: Boolean = true,
    content: (@Composable () -> Unit) = {},
) {
    TODO("Emplear la Modal Navigation Drawer. OJO EN ESTA TIENE QUE HABER ACCESO A TODAS LAS VENTANAS AGRUPADAS POR SECCIONES!!!!!!")
    // IMPORTANTE: RECUERDA ELEVAR EL ESTADO DE COSAS COMO drawerState, gesturesEnabled  Y content !!!!!!!!!!!!!!!
    /*
     * Links relevantes:
     * - https://m3.material.io/components/navigation-drawer/overview
     * - https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ModalNavigationDrawer(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material3.DrawerState,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.unit.Dp,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function0)
     *
     * Botones: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#NavigationDrawerItem(kotlin.Function0,kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Shape,androidx.compose.material3.NavigationDrawerItemColors,androidx.compose.foundation.interaction.MutableInteractionSource)
     */
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
                    modifier = Modifier.size(120.dp))
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
    EtziTheme {
        Scaffold(
            bottomBar = { EtziNavigationBar {} }
        ) { paddingValues -> EtziLogo(paddingValues) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EtziNavigationRailPreview() {
    EtziTheme {
        Scaffold { paddingValues ->
            Row(Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                EtziNavigationRail(onNavigate = {})
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
        val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val scope = rememberCoroutineScope()

        EtziNavigationDrawer(navigationDrawerState, {}) {
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
                bottomBar = { EtziNavigationBar {} }
            ) { paddingValues ->
                EtziLogo(paddingValues)
            }
        }
    }
}