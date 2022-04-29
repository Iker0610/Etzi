package das.losaparecidos.etzi.app.activities.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.R
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            EtziTheme {
                Scaffold(
                    topBar = {
                        SmallTopAppBar(
                            title = { Text("Inicio", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            actions = {
                                IconButton(onClick = { Toast.makeText(context, "TODO: Perfil de usuario", Toast.LENGTH_SHORT).show() }) {
                                    Icon(Icons.Rounded.AccountCircle, null)
                                }
                            }
                        )
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 32.dp, horizontal = 16.dp)
                    ) {
                        Card {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(vertical = 32.dp, horizontal = 64.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_ehu_logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(120.dp))
                                Text(
                                    text = "Etzi",
                                    style = MaterialTheme.typography.displayLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}