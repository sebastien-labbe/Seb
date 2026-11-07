package ca.villageia.ui.village

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.villageia.catalog.BuildingCatalog
import ca.villageia.data.Building
import ca.villageia.ui.theme.Grass

const val COLUMNS = 4
const val ROWS = 6
const val SLOTS = COLUMNS * ROWS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillageScreen(
    onEmptySlot: (Int) -> Unit,
    onBuilding: (Long) -> Unit,
    onSettings: () -> Unit,
    vm: VillageViewModel = viewModel(),
) {
    val buildings by vm.buildings.collectAsState()
    val bySlot = buildings.associateBy { it.slotIndex }

    Scaffold(topBar = { TopAppBar(title = { Text("Mon village") }) }) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(COLUMNS),
            modifier = Modifier.padding(padding).fillMaxSize().background(Grass),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(SLOTS) { slot ->
                val b = bySlot[slot]
                if (b == null) EmptySlot { onEmptySlot(slot) }
                else BuildingSlot(b) { onBuilding(b.id) }
            }
        }
    }
}

@Composable
private fun slotModifier(onClick: () -> Unit) = Modifier
    .aspectRatio(0.8f)
    .clip(RoundedCornerShape(12.dp))
    .clickable(onClick = onClick)

@Composable
private fun EmptySlot(onClick: () -> Unit) {
    Box(
        slotModifier(onClick).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center,
    ) { Icon(Icons.Default.Add, contentDescription = "Ajouter un bâtiment") }
}

@Composable
private fun BuildingSlot(b: Building, onClick: () -> Unit) {
    val emoji = BuildingCatalog.get(b.typeId)?.emoji ?: "🖼️"
    Column(
        slotModifier(onClick).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(emoji, fontSize = 32.sp)
        Text(b.name, fontSize = 10.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
    }
}
