package das.losaparecidos.etzi.app.activities.authentication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen() {
    Scaffold { paddingValues ->
        /**
         * Características:
         *
         * 2 TextBox
         * - Uno para el ldap (usar texto con validación)
         * - Uno para la contraseña (usar los textfield ya creados)
         *
         * - Un switch para elegir si recordar la contraseña o no
         * - Un botón para autenticación por biometrics
         *
         */
        TODO("Rellenar")
    }

}


// Preview
@Preview
@Composable
fun AuthenticationScreenPreview() {
    EtziTheme { AuthenticationScreen() }
}