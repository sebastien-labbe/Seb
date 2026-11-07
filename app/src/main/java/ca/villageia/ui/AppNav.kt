package ca.villageia.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.villageia.ui.building.BuildingScreen
import ca.villageia.ui.catalog.CatalogScreen
import ca.villageia.ui.chat.ChatScreen
import ca.villageia.ui.person.PersonScreen
import ca.villageia.ui.settings.SettingsScreen
import ca.villageia.ui.village.VillageScreen

object Routes {
    const val VILLAGE = "village"
    const val CATALOG = "catalog/{slot}"
    const val BUILDING = "building/{buildingId}"
    const val CHAT = "chat/{personId}"
    const val PERSON = "person/{personId}"
    const val SETTINGS = "settings"
}

// La pile de navigation gère la flèche arrière :
// Fiche personne -> Conversation -> Bâtiment -> Village.
@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(nav, startDestination = Routes.VILLAGE) {
        composable(Routes.VILLAGE) {
            VillageScreen(
                onEmptySlot = { slot -> nav.navigate("catalog/$slot") },
                onBuilding = { id -> nav.navigate("building/$id") },
                onSettings = { nav.navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.CATALOG) { CatalogScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.BUILDING) {
            BuildingScreen(
                onPerson = { id -> nav.navigate("chat/$id") },
                onBack = { nav.popBackStack() },
            )
        }
        composable(Routes.CHAT) {
            ChatScreen(
                onInfo = { id -> nav.navigate("person/$id") },
                onBack = { nav.popBackStack() },
            )
        }
        composable(Routes.PERSON) { PersonScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.SETTINGS) { SettingsScreen(onBack = { nav.popBackStack() }) }
    }
}
