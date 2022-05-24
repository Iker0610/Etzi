package das.losaparecidos.etzi.app.activities.authentication.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import das.losaparecidos.etzi.R
import kotlinx.coroutines.delay


/*******************************************************************************
 ****                             Splash Screen                             ****
 *******************************************************************************/

/**
 * Application's Intro Splash Screen for aesthetic and professional feel purposes.
 *
 * @param onAnimationFinished Callback for splash screen animation end event.
 */
@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit) {

    /*------------------------------------------------
    |                     States                     |
    ------------------------------------------------*/
    var startAnimation by rememberSaveable { mutableStateOf(false) }

    // Animate the opacity of the icon with an dynamic float state
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(delayMillis = 150, durationMillis = 1750)
    )


    /*------------------------------------------------
    |                     Events                     |
    ------------------------------------------------*/

    // Start the animation (icon fades in)
    // then wait 2 seconds showing the icon before finishing
    // ** Due to the key being "true" this effect will launch once at start
    LaunchedEffect(true) {
        startAnimation = true
        delay(2000)
        onAnimationFinished()
    }


    /*------------------------------------------------
    |                 User Interface                 |
    ------------------------------------------------*/
    Splash(alpha = alphaAnim)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Splash(alpha: Float) {
    // Background Box
    Scaffold { padding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .alpha(alpha)
        ) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ehu_logo),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp))
                    Text(
                        text = "Etzi",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}