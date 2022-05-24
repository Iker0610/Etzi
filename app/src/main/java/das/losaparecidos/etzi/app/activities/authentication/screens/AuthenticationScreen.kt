package das.losaparecidos.etzi.app.activities.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.authentication.AuthenticationViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
import das.losaparecidos.etzi.app.ui.components.form.PasswordField
import das.losaparecidos.etzi.app.ui.components.form.ValidatorTextField
import das.losaparecidos.etzi.app.utils.BiometricAuthManager
import das.losaparecidos.etzi.app.utils.BiometricAuthenticationStatus
import das.losaparecidos.etzi.app.utils.DeviceBiometricsSupport
import das.losaparecidos.etzi.app.utils.canBeValidLdap
import das.losaparecidos.etzi.model.entities.AuthUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    onSuccessfulLogin: (AuthUser) -> Unit = {},
    biometricSupportChecker: () -> DeviceBiometricsSupport = { DeviceBiometricsSupport.UNSUPPORTED },
    onBiometricAuthRequested: () -> Unit = {},
) {
    /*************************************************
     **             Variables and States            **
     *************************************************/


    //-----------   Utility variables   ------------//
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current


    //-----------------   States   -----------------//
    var biometricSupport by rememberSaveable { mutableStateOf(biometricSupportChecker()) }

    var showLoginErrorDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricErrorNotPreviousLoggedUserDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricErrorCredentialsNotLongerValidDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricEnrollDialog by rememberSaveable { mutableStateOf(false) }
    var showGenericErrorDialog by rememberSaveable { mutableStateOf(false) }


    /*************************************************
     **                    Events                   **
     *************************************************/

    // On login clicked action
    val onLogin: () -> Unit = {
        // Launch as coroutine in IO to avoid blocking Main(UI) thread
        coroutineScope.launch(Dispatchers.IO) {
            try {
                // Check if login has been successful
                val user = authenticationViewModel.checkUserPasswordLogin()
                if (user != null) {
                    onSuccessfulLogin(user)
                } else {
                    showLoginErrorDialog = !authenticationViewModel.isLoginCorrect
                    authenticationViewModel.backgroundBlockingTaskOnCourse = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                authenticationViewModel.backgroundBlockingTaskOnCourse = false
                showGenericErrorDialog = true
            }
        }
    }

    // On biometrics clicked action
    val onBiometricAuth: () -> Unit = {
        // Check device's support and act accordingly
        biometricSupport = biometricSupportChecker()
        when {
            // If there's not been a previous logged user show error dialog
            authenticationViewModel.biometricAuthenticationStatus == BiometricAuthenticationStatus.NO_CREDENTIALS ->
                showBiometricErrorNotPreviousLoggedUserDialog = true

            // If device supports biometrics but are not configured show enrollment dialog
            biometricSupport == DeviceBiometricsSupport.NOT_CONFIGURED -> showBiometricEnrollDialog = true

            // Else if it is supported, ask for biometrics authorization
            biometricSupport != DeviceBiometricsSupport.UNSUPPORTED && authenticationViewModel.biometricAuthenticationStatus == BiometricAuthenticationStatus.NOT_AUTHENTICATED_YET -> onBiometricAuthRequested()
        }
    }

    //----   On Biometric Auth Status Change   -----//
    when (authenticationViewModel.biometricAuthenticationStatus) {
        BiometricAuthenticationStatus.CREDENTIALS_ERROR -> {
            showBiometricErrorCredentialsNotLongerValidDialog = true
            authenticationViewModel.biometricAuthenticationStatus = BiometricAuthenticationStatus.NOT_AUTHENTICATED_YET
        }
        BiometricAuthenticationStatus.ERROR -> {
            showGenericErrorDialog = true
            authenticationViewModel.biometricAuthenticationStatus = BiometricAuthenticationStatus.NOT_AUTHENTICATED_YET
        }
        else -> {}
    }


    /*************************************************
     **                User Interface               **
     *************************************************/


    /*------------------------------------------------
    |                    Dialogs                     |
    ------------------------------------------------*/

    //----------   Generic Error Dialog   ----------//
    if (showGenericErrorDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.server_error_dialog_title), textAlign = TextAlign.Justify) },
            onDismissRequest = { showGenericErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showGenericErrorDialog = false }) {
                    Text(text = stringResource(id = R.string.ok_button))
                }
            },
        )
    }


    //-----------   Login Error Dialog   -----------//
    if (showLoginErrorDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.incorrect_login_error_message), textAlign = TextAlign.Justify) },
            onDismissRequest = { showLoginErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showLoginErrorDialog = false }) {
                    Text(text = stringResource(R.string.ok_button))
                }
            },
        )
    }

    //---   Biometric Login User Error Dialog   ----//
    if (showBiometricErrorNotPreviousLoggedUserDialog) {
        AlertDialog(
            icon = { Icon(Icons.Rounded.PersonOff, contentDescription = null) },
            title = { Text(text = stringResource(R.string.invalid_account_login_dialog_title), textAlign = TextAlign.Center) },
            text = {
                Text(
                    text = stringResource(R.string.invalid_account_login_dialog_text),
                    textAlign = TextAlign.Justify
                )
            },
            onDismissRequest = { showBiometricErrorNotPreviousLoggedUserDialog = false },
            confirmButton = {
                TextButton(onClick = { showBiometricErrorNotPreviousLoggedUserDialog = false }) {
                    Text(text = stringResource(R.string.ok_button))
                }
            }
        )
    }

    //---   Credentials Not Valid Error Dialog   ---//
    if (showBiometricErrorCredentialsNotLongerValidDialog) {
        AlertDialog(
            icon = { Icon(Icons.Rounded.PersonOff, contentDescription = null) },
            title = { Text(text = stringResource(R.string.saved_credentials_not_longer_valid_dialog_title), textAlign = TextAlign.Center) },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.saved_credentials_not_longer_valid_dialog_text),
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        text = stringResource(R.string.saved_credentials_not_longer_valid_dialog_solution_text),
                        textAlign = TextAlign.Justify
                    )
                }
            },
            onDismissRequest = { showBiometricErrorCredentialsNotLongerValidDialog = false },
            confirmButton = {
                TextButton(onClick = { showBiometricErrorCredentialsNotLongerValidDialog = false }) {
                    Text(text = stringResource(R.string.ok_button))
                }
            }
        )
    }


    //-------   Biometric's Enroll Dialog   --------//
    if (showBiometricEnrollDialog) {
        AlertDialog(
            icon = { Icon(Icons.Rounded.Fingerprint, contentDescription = null) },
            title = { Text(text = stringResource(R.string.no_biometrics_enrolled_dialog_title), textAlign = TextAlign.Center) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(text = stringResource(R.string.no_biometrics_enrolled_dialog_text_1), textAlign = TextAlign.Justify)
                    Text(text = stringResource(R.string.no_biometrics_enrolled_dialog_text_2), textAlign = TextAlign.Justify)

                }
            },
            onDismissRequest = { showBiometricEnrollDialog = false },

            // If the user agrees, take them to settings in order to configure biometric authentication
            confirmButton = {
                TextButton(
                    onClick = {
                        showBiometricEnrollDialog = false
                        BiometricAuthManager.makeBiometricEnroll(context)
                    }
                ) { Text(text = stringResource(R.string.enroll_button)) }
            },
            dismissButton = {
                TextButton(onClick = { showBiometricEnrollDialog = false }) { Text(text = stringResource(R.string.cancel_button)) }
            }
        )
    }


    /*------------------------------------------------
    |                  Main Screen                   |
    ------------------------------------------------*/

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
                Icon(
                    painter = painterResource(id = R.drawable.ic_ehu_logo),
                    contentDescription = "UPV/EHU logo",
                    modifier = Modifier.size(50.dp),
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
                    //isValidUsername(textFieldValue.value) && isNumeric(textFieldValue.value) --> al viewmodel
                    ValidatorTextField(
                        modifier = Modifier.fillMaxWidth(),

                        value = authenticationViewModel.loginUsername,
                        onValueChange = { if (canBeValidLdap(it)) authenticationViewModel.loginUsername = it },

                        label = { Text(text = stringResource(R.string.ldap_title)) },
                        leadingIcon = { Icon(Icons.Filled.Person, null) },

                        isValid = authenticationViewModel.isLoginCorrect,
                        ignoreFirstTime = true,

                        enabled = !authenticationViewModel.backgroundBlockingTaskOnCourse,

                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                    )


                    PasswordField(
                        modifier = Modifier.fillMaxWidth(),

                        value = authenticationViewModel.loginPassword,
                        onValueChange = authenticationViewModel::loginPassword::set,

                        isValid = authenticationViewModel.isLoginCorrect,
                        ignoreFirstTime = true,

                        enabled = !authenticationViewModel.backgroundBlockingTaskOnCourse,

                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                            onDone = {
                                //si el usuario le da al ok del teclado
                                focusManager.clearFocus()
                                //aqui debería haber una llamada a iniciar sesión
                            }
                        ),
                        imeAction = ImeAction.Done
                    )


                    CenteredRow {
                        Text(
                            text = stringResource(R.string.remind_me_label),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Checkbox(
                            checked = authenticationViewModel.rememberLogin,
                            onCheckedChange = authenticationViewModel::rememberLogin::set,
                            enabled = true
                        )
                    }

                    Divider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))

                    if (authenticationViewModel.backgroundBlockingTaskOnCourse) {

                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(32.dp)
                                .size(40.dp),
                            strokeWidth = 5.dp
                        )

                    } else {
                        //--------------   Login Button   --------------//
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onLogin,

                            // We only enable the button if user and password have possible valid values (does not check if user and pass are in database)
                            enabled = authenticationViewModel.loginUsername.isNotBlank() && authenticationViewModel.loginPassword.isNotBlank()
                        ) {
                            Text(text = stringResource(R.string.login_label))
                        }

                        if (biometricSupport != DeviceBiometricsSupport.UNSUPPORTED) {
                            OutlinedButton(
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
}