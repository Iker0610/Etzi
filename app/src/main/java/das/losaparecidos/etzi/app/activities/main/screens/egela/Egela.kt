package das.losaparecidos.etzi.app.activities.main.screens.egela

import android.webkit.CookieManager
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.screens.account.AccountIcon
import das.losaparecidos.etzi.app.activities.main.viewmodels.AccountViewModel
import das.losaparecidos.etzi.app.activities.main.viewmodels.EgelaViewModel
import das.losaparecidos.etzi.app.ui.components.CenteredBox
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EgelaScreen(
    windowSizeClass: WindowSizeClass,
    onMenuOpen: () -> Unit,
    egelaViewModel: EgelaViewModel,
    onNavigate: () -> Unit,
    accountViewModel: AccountViewModel
) {
    val initialized by rememberSaveable { mutableStateOf(true) }
    var loadingData by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = MainActivityScreens.Egela.title(LocalContext.current)) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        IconButton(onClick = onMenuOpen) {
                            Icon(Icons.Rounded.Menu, null)
                        }
                    }
                },
                actions = {
                    AccountIcon(accountViewModel, onNavigate)
                }
            )
        }
    ) { paddingValues ->

        val egela = rememberWebViewState("https://egela.ehu.eus")

        Crossfade(
            targetState = loadingData, animationSpec = tween(500), modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { showLoader ->
            if (showLoader) {

                CenteredBox(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 5.dp, modifier = Modifier.size(48.dp))
                }
            } else {
                WebView(egela, Modifier.fillMaxSize())
            }
        }

        LaunchedEffect(initialized) {
            (Dispatchers.IO) {
                try {
                    val ldap = egelaViewModel.loggedUser.ldap
                    val password = egelaViewModel.loggedUser.password

                    val egelaData: JSONObject = getCookieEgela(ldap, password)

                    val cookie = egelaData.get("cookie").toString().substring(0, egelaData.get("cookie").toString().indexOf("/") + 1)
                    CookieManager.getInstance().setCookie("https://egela.ehu.eus", cookie)

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    loadingData = false
                }
            }
        }
    }
}

@OptIn(InternalAPI::class)
private suspend fun getCookieEgela(ldap: String, password: String): JSONObject = withContext(Dispatchers.IO) {

    //////////////////////////////////////// PRIMERA PETICIÓN ////////////////////////////////////////

    // Obtenemos el loginToken y la cookie inicial

    // Creamos el cliente
    val client = HttpClient(CIO) {
        install(HttpTimeout)
    }

    // Hacemos la petición
    val responseGetToken: HttpResponse = client.get("https://egela.ehu.eus") {
        headers {
            append(HttpHeaders.Accept, "text/html")
        }
    }

    // Conseguimos el token
    val token = getToken(responseGetToken.body())
    if (token == "fail") {
        return@withContext JSONObject()
    }


    val primeraCookie = responseGetToken.headers["Set-Cookie"]


    //////////////////////////////////////// SEGUNDA PETICIÓN ////////////////////////////////////////

    // Hacemos la petición
    val response1: HttpResponse = client.post(
        "https://egela.ehu.eus/login/index.php"
    ) {
        headers {
            append("Cookie", primeraCookie.toString())
        }
        body = FormDataContent(Parameters.build {
            append("logintoken", token)
            append("username", ldap)
            append("password", password)
        })
    }

    val status1 = response1.status.value

    val resultado = JSONObject()

    if (status1 == 303) { // Comprobamos la respuesta de la petición
        val cookieEntera = response1.headers["Set-Cookie"]
        val location = response1.headers["Location"]

        val cookie = cookieEntera.toString().substring(0, cookieEntera.toString().indexOf("/") + 1) // Obtenemos la cookie

        //////////////////////////////////////// TERCERA PETICIÓN ////////////////////////////////////////

        // Hacemos la petición
        val response2: HttpResponse = client.post(location.toString()) {
            headers {
                append("Cookie", cookie)
            }
        }
        if (response2.status.value == 303) { // Comprobamos la respuesta de la petición
            resultado.put("cookie", cookie)
        }

    } else {
        // Si el usuario no tiene cuenta en egela (Si es un usuario de prueba no tiene cuenta real de la universidad)
        return@withContext JSONObject()
    }

    client.close()
    return@withContext resultado
}

private fun getToken(body: String): String {
    try {
        // Obtenemos el inputtoken
        val inputToken = "<input type=\"hidden\" name=\"logintoken\" value=\".*\""
        val regex = inputToken.toRegex()
        val token = regex.find(body)
        if (token != null) {
            return token.value.substring(46, token.value.length - 1)
        }
    } catch (e: Exception) {
    }
    return "fail"
}
