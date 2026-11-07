package ca.villageia.ui.catalog

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.villageia.catalog.BuildingCatalog
import ca.villageia.catalog.BuildingType
import ca.villageia.repository
import kotlinx.coroutines.launch

class CatalogViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val repo = app.repository
    private val slot: Int = checkNotNull(handle["slot"])

    fun add(type: BuildingType, done: () -> Unit) {
        viewModelScope.launch {
            repo.addBuilding(type, slot)
            done()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(onBack: () -> Unit, vm: CatalogViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choisir un bâtiment") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize()) {
            BuildingCatalog.byCategory.forEach { (category, types) ->
                item(key = category.name) {
                    Text(
                        category.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
                    )
                }
                items(types.size, key = { types[it].id }) { i ->
                    val t = types[i]
                    Row(
                        Modifier.fillMaxWidth().clickable { vm.add(t, onBack) }.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(t.emoji, fontSize = 28.sp)
                        Text(t.defaultName)
                    }
                }
            }
        }
    }
}
