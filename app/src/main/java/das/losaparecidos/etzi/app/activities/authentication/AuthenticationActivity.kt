package das.losaparecidos.etzi.app.activities.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import das.losaparecidos.etzi.app.ui.theme.EtziTheme

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EtziTheme {
                TODO("POR IMPLEMENTAR")
            }
        }
    }
}