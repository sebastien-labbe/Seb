package ca.villageia.ui.building

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.villageia.catalog.Expertises
import ca.villageia.catalog.Personalities
import ca.villageia.data.Building
import ca.villageia.data.Person
import ca.villageia.repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BuildingViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val id: Long = checkNotNull(handle["buildingId"])
    private val repo = app.repository
    val building = repo.building(id).stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val persons = repo.persons(id).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingScreen(onPerson: (Long) -> Unit, onBack: () -> Unit, vm: BuildingViewModel = viewModel()) {
    val building: Building? by vm.building.collectAsState()
    val persons: List<Person> by vm.persons.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(building?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(persons, key = { it.id }) { p ->
                Card(Modifier.fillMaxWidth().clickable { onPerson(p.id) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text(p.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${p.customExpertise ?: Expertises.label(p.expertiseId)} · " +
                                (Personalities.all.firstOrNull { it.id == p.personalityId }?.label ?: ""),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}
