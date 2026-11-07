package ca.villageia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ca.villageia.ui.AppNav
import ca.villageia.ui.theme.VillageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VillageTheme { AppNav() } }
    }
}
