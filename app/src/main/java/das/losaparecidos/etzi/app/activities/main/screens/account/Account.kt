package das.losaparecidos.etzi.app.activities.main.screens.account

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.authentication.AuthenticationActivity
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.composables.StudentDataSection
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.ui.components.*
import das.losaparecidos.etzi.app.ui.components.form.SectionTitle
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.LanguagePickerDialog
import das.losaparecidos.etzi.model.entities.Student
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess


@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel,
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val student by accountViewModel.studentData.collectAsState(initial = Student("", "", "", "", ""))
    val prefLanguage by accountViewModel.prefLang.collectAsState(accountViewModel.currentSetLang)
    val profilePicture: Bitmap? = accountViewModel.profilePicture
    var showSelectLangDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmLogoutDialog by rememberSaveable { mutableStateOf(false) }

    /*************************************************
     **                    Events                   **
     *************************************************/
    val toastMsg = stringResource(R.string.profile_not_taken_toast_msg)

    // Camera photo
    val imagePickerLauncherFromCamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
        if (pictureTaken) accountViewModel.setProfileImage()
        else Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
    }

    // Gallery photo
    val imagePickerLauncherFromGallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { pictureTaken ->
        pictureTaken?.let { uri ->
            GlobalScope.launch(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    accountViewModel.setProfileImage(BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri)))
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)

                    bitmap.let { bm -> accountViewModel.setProfileImage(bm) }
                }
            }
        }
    }

    fun onEditImageRequest(fromCamera: Boolean) {

        val profileImageDir = File(context.cacheDir, "images/profile/")
        Files.createDirectories(profileImageDir.toPath())

        val newProfileImagePath = File.createTempFile(student.ldap, ".png", profileImageDir)
        val contentUri: Uri = getUriForFile(context, "das.losaparecidos.etzi.fileprovider", newProfileImagePath)
        accountViewModel.profilePicturePath = newProfileImagePath.path

        if (fromCamera) imagePickerLauncherFromCamera.launch(contentUri)
        else imagePickerLauncherFromGallery.launch("image/*")

    }


    if (showSelectLangDialog) {
        LanguagePickerDialog(
            selectedLanguage = prefLanguage,
            onLanguageSelected = {
                accountViewModel.onLanguageChanged(it, context)
                showSelectLangDialog = false
            },
            onDismiss = { showSelectLangDialog = false }
        )
    }
    //-----------   Logout confirm dialog   -----------//
    if (showConfirmLogoutDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.confirm_logout_message), textAlign = TextAlign.Justify) },
            onDismissRequest = { showConfirmLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmLogoutDialog = false
                    accountViewModel.onLogout()
                    context.startActivity(Intent(context, AuthenticationActivity::class.java))
                    exitProcess(0)
                }) {
                    Text(text = stringResource(R.string.ok_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmLogoutDialog = false }) {
                    Text(text = stringResource(R.string.cancel_button))
                }
            }
        )
    }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }

    var openChooseImageDialog by rememberSaveable { mutableStateOf(false) }

    if (openChooseImageDialog) {

        Dialog(onDismissRequest = { openChooseImageDialog = false }) {
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {

                    CenteredRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Icon(Icons.Rounded.Collections, null)
                        TextButton(
                            onClick = {
                                onEditImageRequest(fromCamera = false)
                                openChooseImageDialog = false
                            }
                        ) { Text(stringResource(id = R.string.chooseFromGallery)) }
                    }

                    CenteredRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Icon(Icons.Rounded.CameraAlt, null)
                        TextButton(
                            onClick = {
                                onEditImageRequest(fromCamera = true)
                                openChooseImageDialog = false
                            }
                        ) { Text(stringResource(id = R.string.takeFromCamera)) }
                    }
                }
            }
        }

    }



    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DynamicLargeMediumTopAppBar(
                windowSizeClass = windowSizeClass,
                title = { Text(text = MainActivityScreens.Account.title(context)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        CenteredColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
        ) {

            /*------------------------------------------------
            |                    Dialogs                     |
            ------------------------------------------------*/


            CenteredColumn() {
                ElevatedCard(modifier = Modifier.padding(horizontal = 32.dp)) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (profilePicture == null) {
                            //LoadingImagePlaceholder(size = 138.dp)
                        } else {


                            Image(
                                bitmap = profilePicture.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clickable { openChooseImageDialog = true }
                            )

                            FilledTonalIconButton(onClick = { openChooseImageDialog = true }, Modifier.size(42.dp)) {
                                Icon(Icons.Rounded.Edit, contentDescription = null, Modifier.size(24.dp))
                            }

                        }
                    }

                }


//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .padding(bottom = 16.dp, end = 8.dp)
//                        .clip(CircleShape)
//                    //.clickable(onClick = ::onEditImageRequest)
//                ) {
//
//                    Icon(Icons.Rounded.Circle, contentDescription = null, Modifier.size(34.dp), tint = MaterialTheme.colorScheme.primary)
//                    Icon(Icons.Rounded.Edit, contentDescription = null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.surface)
//                }
            }

            Column(
                Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                MaterialDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))


                SectionTitle(
                    icon = Icons.Rounded.Badge, text = stringResource(id = R.string.student_data), modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
                StudentDataSection(student = student, Modifier.padding(horizontal = 16.dp))


                MaterialDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))


                //------------   Language Section   ------------//

                SectionTitle(icon = Icons.Rounded.Settings, text = stringResource(id = R.string.settings), modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    icon = { Icon(Icons.Rounded.Language, null, Modifier.padding(top = 7.dp)) },
                    secondaryText = { Text(text = prefLanguage.name) },
                    modifier = Modifier.clickable {
                        showSelectLangDialog = true
                    }
                ) {
                    Text(text = stringResource(R.string.language_label))
                }
                //------------   Logout Section    ------------//

                MaterialDivider(Modifier.padding(vertical = 8.dp))
            }

            OutlinedButton(
                onClick = {
                    showConfirmLogoutDialog = true
                }
            ) {
                Icon(Icons.Rounded.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.logout_label))
            }
        }
    }
}

@Composable
fun AccountIcon(accountViewModel: AccountViewModel, onNavigate: () -> Unit) {
    val profilePicture: Bitmap? = accountViewModel.profilePicture
    Box(Modifier.padding(16.dp)) {
        if (profilePicture == null) {
            LoadingImagePlaceholder(size = 28.dp)
        } else {
            Image(
                bitmap = profilePicture.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onNavigate() },
            )
        }
    }
}

/*************************************************
 **          Image Loading Placeholder          **
 *************************************************/

@Composable
fun LoadingImagePlaceholder(size: Dp = 140.dp) {
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
        Icons.Rounded.AccountCircle, contentDescription = null,
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
    EtziTheme {
        AccountScreen(viewModel(), WindowSizeClass.calculateFromSize(DpSize(300.dp, 300.dp))) {}
    }
}