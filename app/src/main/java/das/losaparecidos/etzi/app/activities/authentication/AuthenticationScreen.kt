package das.losaparecidos.etzi.app.activities.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.form.PasswordField
import das.losaparecidos.etzi.app.ui.components.form.ValidatorTextField
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.DeviceBiometricsSupport
import das.losaparecidos.etzi.model.entities.AuthUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    onSuccessfulLogin: (AuthUser) -> Unit = {},
    biometricSupportChecker: () -> DeviceBiometricsSupport = { DeviceBiometricsSupport.UNSUPPORTED },
    onBiometricAuth: () -> Unit = {},
) {
    //-----------   Utility variables   ------------//
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    //-----------   Form inputs   ------------//
    val (ldapValue, setLdapValue) = rememberSaveable { mutableStateOf("") }
    val (passwordValue, setPasswordValue) = rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val isChecked = rememberSaveable { mutableStateOf(false) }
    val displayProgressBar = rememberSaveable { mutableStateOf(false) }


    //-----------------   States   -----------------//
    var biometricSupport by rememberSaveable { mutableStateOf(biometricSupportChecker()) }

    var showSignInErrorDialog by rememberSaveable { mutableStateOf(false) }
    var showLoginErrorDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricErrorNotPreviousLoggedUserDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricErrorCredentialsNotLongerValidDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricEnrollDialog by rememberSaveable { mutableStateOf(false) }
    var showGenericErrorDialog by rememberSaveable { mutableStateOf(false) }


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
        CenteredColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            CenteredRow {
                Image(
                    painter = painterResource(id = R.drawable.ic_ehu_logo),
                    contentDescription = "UPV/EHU logo",
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            // padding 32.dp verticales
            Spacer(modifier = Modifier.height(32.dp))


            ElevatedCard(modifier = Modifier.padding(32.dp)) {
                CenteredColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.login_label),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    //campos del formulario, el cual el de la contraseña tiene
                    //para ver la contraseña que estás escribiendo.
                    ValidatorTextField(value = ldapValue, onValueChange = setLdapValue)

                    TransparentTextField(
                        textFieldValue = ldapValue,
                        textLabel = "LDAP",
                        keyboardType = KeyboardType.Number,
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        imeAction = ImeAction.Next,
                    )

                    PasswordField(
                        value = passwordValue,
                        onValueChange = setPasswordValue,
                        modifier = Modifier.fillMaxWidth()
                    )


                    CenteredRow {
                        Text(
                            text = stringResource(R.string.remind_me_label),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Checkbox(
                            checked = isChecked.value,
                            onCheckedChange = { isChecked.value = it },
                            enabled = true
                        )
                    }

                    Divider()

                    RoundedButton(
                        text = stringResource(R.string.login_label),
                        displayProgressBar = displayProgressBar.value,
                        onClick = {
                            // aser kositas cuando le den al botón
                            //onLogin(emailValue.value, passwordValue.value, isChecked.value)
                            // Poner cuando inicemos sesión: displayProgressBar.value = true
                        },
                    )

                    if (biometricSupport != DeviceBiometricsSupport.UNSUPPORTED) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onBiometricAuth
                        ) {
                            Icon(Icons.Filled.Fingerprint, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.biometrics_button))
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    displayProgressBar: Boolean = false,
    onClick: () -> Unit,
) {
    if (!displayProgressBar) {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
        ) {
            Text(text = text)
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    textFieldValue: MutableState<String>,
    textLabel: String,
    maxChar: Int? = null,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions,
    imeAction: ImeAction,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enable: Boolean = true,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = textFieldValue.value.take(maxChar ?: 40),
        onValueChange = { textFieldValue.value = it },
        label = {
            Text(text = textLabel)
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        enabled = enable,
    )
}

// Preview
@Preview
@Composable
fun AuthenticationScreenPreview() {
    EtziTheme { AuthenticationScreen() }
}