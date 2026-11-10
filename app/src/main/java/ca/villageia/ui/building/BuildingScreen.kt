package ca.villageia.ui.building

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.villageia.catalog.Expertises
import ca.villageia.catalog.Personalities
import ca.villageia.repository
import ca.villageia.ui.Avatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BuildingViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val id: Long = checkNotNull(handle["buildingId"])
    private val repo = app.repository
    val building = repo.building(id).stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val persons = repo.persons(id).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addPerson(onCreated: (Long) -> Unit) {
        viewModelScope.launch { onCreated(repo.addPerson(id)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingScreen(
    onPerson: (Long) -> Unit,
    onEditPerson: (Long) -> Unit,
    onBack: () -> Unit,
    vm: BuildingViewModel = viewModel(),
) {
    val building by vm.building.collectAsState()
    val persons by vm.persons.collectAsState()
    var askAdd by remember { mutableStateOf(false) }

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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { askAdd = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Ajouter quelqu'un") },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 88.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(persons, key = { it.id }) { p ->
                Card(Modifier.fillMaxWidth().clickable { onPerson(p.id) }) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Avatar(p.avatarSeed, 48.dp)
                        Column(Modifier.weight(1f)) {
                            Text(p.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${p.customExpertise ?: Expertises.label(p.expertiseId)} · " +
                                    (Personalities.all.firstOrNull { it.id == p.personalityId }?.label ?: ""),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        IconButton(onClick = { onEditPerson(p.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                    }
                }
            }
        }
    }

    if (askAdd) {
        AlertDialog(
            onDismissRequest = { askAdd = false },
            title = { Text("Ajouter quelqu'un") },
            text = { Text("Au hasard : nom, avatar, personnalité et expertise sont tirés pour toi. Personnalisé : tu règles tout toi-même.") },
            confirmButton = {
                TextButton(onClick = { askAdd = false; vm.addPerson { onEditPerson(it) } }) { Text("Personnalisé") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { askAdd = false; vm.addPerson { } }) { Text("🎲 Au hasard") }
                    TextButton(onClick = { askAdd = false }) { Text("Annuler") }
                }
            },
        )
    }
}
