package das.losaparecidos.etzi.app.activities.main.screens.account

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.composables.StudentDataSection
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.ListItem
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.AppLanguage
import das.losaparecidos.etzi.app.utils.LanguagePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel,
    windowSizeClass: WindowSizeClass,
) {
    val context = LocalContext.current
    val rememberNavController = rememberNavController()
    val profilePicture: Bitmap? by mutableStateOf(null)
    val student = accountViewModel.studentData
    var showSelectLangDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Account.title(context)) },
                navigationIcon = {
                    IconButton(onClick = { rememberNavController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = { rememberNavController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    }

                })
        }
    ) { paddingValues ->
        CenteredColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(vertical = 16.dp)
        ) {
            /*------------------------------------------------
            |                    Dialogs                     |
            ------------------------------------------------*/

            if (showSelectLangDialog) {
                // TODO revisar esto para conectarlo con el viewmodel
                LanguagePickerDialog(
                    title = stringResource(R.string.select_lang_dialog_title),
                    selectedLanguage = AppLanguage.ES,
                    onLanguageSelected = { /*TODO aquí debería ir la llamada del viewmodel preferencesViewModel.changeLang(it, context);*/ showSelectLangDialog = false },
                    onDismiss = { showSelectLangDialog = false }
                )
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(Modifier.padding(16.dp)) {
                    if (profilePicture == null) {
                        LoadingImagePlaceholder(size = 120.dp)
                    } else {
                        //TODO hacer que al hacer click abra la ventanita para seleccionar una foto
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Rounded.Image, contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                            )
                            /*Image(
                                bitmap = profilePicture.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                            )*/
                            /*Icon(
                                Icons.Rounded.ExitToApp, contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                            )*/
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp, end = 8.dp)
                        .clip(CircleShape)
                    //.clickable(onClick = ::onEditImageRequest)
                ) {

                    //Icon(Icons.Rounded.Circle, contentDescription = null, Modifier.size(34.dp), tint = MaterialTheme.colorScheme.primary)
                    Icon(Icons.Rounded.Edit, contentDescription = null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.surface)
                }
            }

            StudentDataSection(student = student)
            
            MaterialDivider(Modifier.padding(top = 32.dp, bottom = 16.dp))


            //------------   Language Section   ------------//

            ListItem(
                // TODO poner el idioma actualmente seleccionado
                icon = { Icon(Icons.Rounded.Language, null, Modifier.padding(top = 7.dp)) },
                secondaryText = { Text(text = "Idioma"/*prefLanguage.language*/) },
                modifier = Modifier.clickable { showSelectLangDialog = true }
            ) {
                Text(text = "Idioma pruebita"/*stringResource(R.string.app_lang_setting_title)*/)
            }
            //------------   Logout Section    ------------//
            ListItem(
                // TODO poner el idioma actualmente seleccionado
                icon = { Icon(Icons.Rounded.Logout, null, Modifier.padding(top = 7.dp)) },
                modifier = Modifier.clickable { showSelectLangDialog = true }
            ) {
                Text(text = stringResource(id = R.string.logout_label)/*stringResource(R.string.app_lang_setting_title)*/)
            }
        }
    }
}

/*************************************************
 **          Image Loading Placeholder          **
 *************************************************/

@Composable
private fun LoadingImagePlaceholder(size: Dp = 140.dp) {
    // Creates an `InfiniteTransition` that runs infinite child animation values.
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        // `infiniteRepeatable` repeats the specified duration-based `AnimationSpec` infinitely.
        animationSpec = infiniteRepeatable(
            // The `keyframes` animates the value by specifying multiple timestamps.
            animation = keyframes {
                // One iteration is 1000 milliseconds.
                durationMillis = 1000
                // 0.7f at the middle of an iteration.
                0.7f at 500
            },
            // When the value finishes animating from 0f to 1f, it repeats by reversing the
            // animation direction.
            repeatMode = RepeatMode.Reverse
        )
    )

    Icon(
        Icons.Rounded.Image, contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .alpha(alpha),
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AccountScreenPreview() {
    EtziTheme() {
        AccountScreen(viewModel(),WindowSizeClass.calculateFromSize(DpSize(300.dp, 300.dp)))
    }
}