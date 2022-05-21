package das.losaparecidos.etzi.app.activities.main.screens.account

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.viewmodel.compose.viewModel
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.activities.authentication.AuthenticationActivity
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.composables.StudentDataSection
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredColumn
import das.losaparecidos.etzi.app.ui.components.DynamicLargeMediumTopAppBar
import das.losaparecidos.etzi.app.ui.components.ListItem
import das.losaparecidos.etzi.app.ui.components.MaterialDivider
import das.losaparecidos.etzi.app.ui.components.form.SectionTitle
import das.losaparecidos.etzi.app.ui.theme.EtziTheme
import das.losaparecidos.etzi.app.utils.LanguagePickerDialog
import das.losaparecidos.etzi.model.entities.Student
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel,
    windowSizeClass: WindowSizeClass,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val student by accountViewModel.studentData.collectAsState(initial = Student("", "", "", "", ""))
    val prefLanguage by accountViewModel.prefLang.collectAsState(accountViewModel.currentSetLang)
    val profilePicture: Bitmap? = accountViewModel.profilePicture
    var showSelectLangDialog by rememberSaveable { mutableStateOf(false) }

    /*************************************************
     **                    Events                   **
     *************************************************/
    val toastMsg = stringResource(R.string.profile_not_taken_toast_msg)

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
        if (pictureTaken) accountViewModel.setProfileImage()
        else Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
    }

    fun onEditImageRequest() {
        val profileImageDir = File(context.cacheDir, "images/profile/")
        Files.createDirectories(profileImageDir.toPath())

        val newProfileImagePath = File.createTempFile(student.ldap, ".png", profileImageDir)
        val contentUri: Uri = getUriForFile(context, "das.losaparecidos.etzi.fileprovider", newProfileImagePath)
        accountViewModel.profilePicturePath = newProfileImagePath.path

        imagePickerLauncher.launch(contentUri)
    }


    if (showSelectLangDialog) {
        LanguagePickerDialog(
            selectedLanguage = prefLanguage,
            onLanguageSelected = {
                accountViewModel.onLanguageChanged(it, context)
                showSelectLangDialog = false },
            onDismiss = { showSelectLangDialog = false }
        )
    }


    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember { TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DynamicLargeMediumTopAppBar(
                windowSizeClass = windowSizeClass,
                title = { Text(text = MainActivityScreens.Account.title(context)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
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

            Box(contentAlignment = Alignment.BottomEnd) {
                Box {
                    if (profilePicture == null) {
                        //LoadingImagePlaceholder(size = 138.dp)
                    } else {
                        Image(
                            bitmap = profilePicture.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }


                FilledTonalIconButton(onClick = { onEditImageRequest() }, Modifier.size(32.dp)) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(20.dp))
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
                    accountViewModel.onLogout()
                    context.startActivity(Intent(context, AuthenticationActivity::class.java))
                    exitProcess(0)
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
fun AccountIcon(accountViewModel: AccountViewModel, onNavigate: ()-> Unit){
    val profilePicture: Bitmap? = accountViewModel.profilePicture
    Box(Modifier.padding(16.dp)) {
        if (profilePicture == null) {
            LoadingImagePlaceholder(size = 36.dp)
        } else {
            Image(
                bitmap = profilePicture.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
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
    EtziTheme{
        AccountScreen(viewModel(), WindowSizeClass.calculateFromSize(DpSize(300.dp, 300.dp))) {}
    }
}