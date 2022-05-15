package das.losaparecidos.etzi.app.activities.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.CenteredRow
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
    val ldapValue = rememberSaveable { mutableStateOf("") }
    val passwordValue = rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val isChecked = rememberSaveable { mutableStateOf(false) }
    val displayProgressBar = rememberSaveable{mutableStateOf(false) }


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
        CenteredColumn(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            ) {
            Column{
                CenteredRow() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ehu_logo),
                        contentDescription = "UPV/EHU logo",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            // padding 32.dp verticales
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedCard(modifier = Modifier.padding(32.dp)){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.login_label),
                            fontWeight = FontWeight.Bold,
                            //modifier = Modifier.padding(16.dp)
                        )
                    }
                    //campos del formulario, el cual el de la contraseña tiene
                    //para ver la contraseña que estás escribiendo.
                    TransparentTextField(
                        textFieldValue = ldapValue,
                        textLabel = "LDAP",
                        keyboardType = KeyboardType.Email,
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        imeAction = ImeAction.Next,
                        modifier = Modifier.padding(16.dp)
                    )

                    TransparentTextField(
                        textFieldValue = passwordValue,
                        textLabel = stringResource(R.string.password_placeholder),
                        keyboardType = KeyboardType.Password,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                //cuando haya hecho tap en el OK del teclado, iniciamos sesión tb
                            }
                        ),
                        imeAction = ImeAction.Done,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisibility = !passwordVisibility
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisibility) {
                                        Icons.Default.Visibility
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = "Toggle Password Icon"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        modifier = Modifier.padding(16.dp)
                    )

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = stringResource(R.string.remind_me_label),
                            modifier = Modifier.padding(13.dp)
                        )
                        Checkbox(
                            checked = isChecked.value,
                            onCheckedChange = { isChecked.value = it },
                            enabled = true
                        )
                    }
                    Divider(modifier = Modifier.padding(top = 7.dp, bottom = 1.dp))
                    Row (modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(2.dp)){
                        RoundedButton(
                            text = stringResource(R.string.login_label),
                            displayProgressBar = displayProgressBar.value,
                            onClick = {
                                // aser kositas cuando le den al botón
                                //onLogin(emailValue.value, passwordValue.value, isChecked.value)
                                // Poner cuando inicemos sesión: displayProgressBar.value = true
                            },

                        )
                    }
                    if (biometricSupport != DeviceBiometricsSupport.UNSUPPORTED) {
                        Divider(modifier = Modifier.padding(top = 7.dp, bottom = 1.dp))
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
    onClick: () -> Unit
) {
    if (!displayProgressBar) {
        Button(
            modifier = modifier
                .width(280.dp)
                .height(50.dp),
            onClick = onClick,
            shape = RoundedCornerShape(50),
        ) {
            Text(
                text = text,
            )
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
    enable: Boolean = true
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