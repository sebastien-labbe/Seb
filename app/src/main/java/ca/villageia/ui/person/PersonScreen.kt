package ca.villageia.ui.person

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
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
import ca.villageia.data.Person
import ca.villageia.data.randomized
import ca.villageia.repository
import ca.villageia.ui.Avatar
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PersonViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val id: Long = checkNotNull(handle["personId"])
    private val repo = app.repository
    val person = repo.person(id).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun save(p: Person) { viewModelScope.launch { repo.updatePerson(p) } }
    fun delete(done: () -> Unit) { viewModelScope.launch { repo.deletePerson(id); done() } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(onBack: () -> Unit, vm: PersonViewModel = viewModel()) {
    val person by vm.person.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fiche personne") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        },
    ) { padding ->
        val p = person
        if (p != null) PersonForm(p, vm, onBack, Modifier.padding(padding))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PersonForm(p: Person, vm: PersonViewModel, onBack: () -> Unit, modifier: Modifier) {
    var name by remember { mutableStateOf(p.name) }
    var notes by remember { mutableStateOf(p.personalityNotes) }
    var pickExpertise by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar(p.avatarSeed, 88.dp)
            OutlinedButton(onClick = { vm.save(p.copy(avatarSeed = UUID.randomUUID().toString())) }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Autre avatar")
            }
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; vm.save(p.copy(name = it)) },
            label = { Text("Nom") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Expertise", style = MaterialTheme.typography.labelLarge)
        OutlinedButton(onClick = { pickExpertise = true }, modifier = Modifier.fillMaxWidth()) {
            Text(p.customExpertise ?: Expertises.label(p.expertiseId))
        }
        Text("Personnalité", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Personalities.all.forEach { pers ->
                FilterChip(
                    selected = pers.id == p.personalityId,
                    onClick = { vm.save(p.copy(personalityId = pers.id)) },
                    label = { Text(pers.label) },
                )
            }
        }
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it; vm.save(p.copy(personalityNotes = it)) },
            label = { Text("Précisions (optionnel)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Niveau d'intelligence", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("FAST" to "Rapide", "BALANCED" to "Équilibré", "EXPERT" to "Expert").forEach { (value, label) ->
                FilterChip(
                    selected = p.tier == value,
                    onClick = { vm.save(p.copy(tier = value)) },
                    label = { Text(label) },
                )
            }
        }
        Button(onClick = { val r = p.randomized(); name = r.name; vm.save(r) }, modifier = Modifier.fillMaxWidth()) {
            Text("🎲 Tout au hasard")
        }
        OutlinedButton(
            onClick = { confirmDelete = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Supprimer cette personne")
        }
    }

    if (pickExpertise) {
        AlertDialog(
            onDismissRequest = { pickExpertise = false },
            title = { Text("Expertise") },
            confirmButton = { TextButton(onClick = { pickExpertise = false }) { Text("Fermer") } },
            text = {
                LazyColumn(Modifier.heightIn(max = 420.dp)) {
                    Expertises.byCategory.forEach { (category, list) ->
                        item(key = "cat_$category") {
                            Text(
                                category,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                            )
                        }
                        items(list, key = { it.id }) { e ->
                            Text(
                                e.label,
                                color = if (e.id == p.expertiseId) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                                    .clickable { vm.save(p.copy(expertiseId = e.id, customExpertise = null)); pickExpertise = false }
                                    .padding(vertical = 10.dp),
                            )
                        }
                    }
                }
            },
        )
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Supprimer ${p.name} ?") },
            text = { Text("Sa conversation sera supprimée.") },
            confirmButton = { TextButton(onClick = { confirmDelete = false; vm.delete(onBack) }) { Text("Supprimer") } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Annuler") } },
        )
    }
}
