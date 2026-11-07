package ca.villageia.ui.village

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.villageia.catalog.BuildingCatalog
import ca.villageia.repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VillageViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = app.repository
    private val prefs = app.getSharedPreferences("village", 0)

    val buildings = repo.buildings.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init { seedFirstLaunch() }

    // Village de départ : 4 bâtiments, une personne chacun (une seule fois).
    private fun seedFirstLaunch() {
        if (prefs.getBoolean("seeded", false)) return
        viewModelScope.launch {
            if (repo.isEmpty()) {
                listOf("maison_a", "clinique", "garage_a", "ecole").forEachIndexed { slot, id ->
                    BuildingCatalog.get(id)?.let { repo.addBuilding(it, slot) }
                }
            }
            prefs.edit().putBoolean("seeded", true).apply()
        }
    }

    fun remove(id: Long) = viewModelScope.launch { repo.removeBuilding(id) }
}
