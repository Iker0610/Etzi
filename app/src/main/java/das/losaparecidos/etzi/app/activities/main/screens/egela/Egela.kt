package das.losaparecidos.etzi.app.activities.main.screens.egela

import android.webkit.CookieManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import das.losaparecidos.etzi.app.activities.main.MainActivityScreens
import das.losaparecidos.etzi.app.activities.main.viewmodels.EgelaViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.*
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EgelaScreen(windowSizeClass: WindowSizeClass, onMenuOpen: () -> Unit, egelaViewModel: EgelaViewModel) {

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
                })
        }
    ) { paddingValues ->

        val ldap = egelaViewModel.loggedUser.ldap
        val pass = egelaViewModel.loggedUser.password
        val datosEgela: JSONObject = runBlocking{getCookieEgela(ldap, pass)}
        val egela = rememberWebViewState("https://egela.ehu.eus")


        WebView(
            egela,
            Modifier.padding(paddingValues)
        )

        try {
            val cookie = datosEgela.get("cookie").toString().substring(0, datosEgela.get("cookie").toString().indexOf("/") + 1)
            CookieManager.getInstance().setCookie("https://egela.ehu.eus", cookie)
        }catch (e: Exception){
            pass
        }
    }
}

@OptIn(InternalAPI::class)
suspend fun getCookieEgela(ldap: String, password: String): JSONObject = withContext(Dispatchers.IO) {

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
    val token = getToken(responseGetToken.body<String>())
    if (token == "fail") {
        return@withContext JSONObject()
    }


    val primeraCookie = responseGetToken.headers.get("Set-Cookie")


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
        val cookieEntera = response1.headers.get("Set-Cookie")
        val location = response1.headers.get("Location")

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

    }else{
        // Si el usuario no tiene cuenta en egela (Si es un usuario de prueba no tiene cuenta real de la universidad)
        return@withContext JSONObject()
    }

    client.close()
    return@withContext resultado
}

fun getToken(body: String): String {
    try {
        // Obtenemos el inputtoken
        val inputToken = "<input type=\"hidden\" name=\"logintoken\" value=\".*\""
        val regex = inputToken.toRegex()
        val token = regex.find(body)
        if (token != null) {
            return token.value.substring(46, token.value.length - 1)
        }
    } catch (e: Exception) { }
    return "fail"
}
