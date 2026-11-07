package ca.villageia.ui.village

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ca.villageia.ui.theme.Grass

const val COLUMNS = 4
const val ROWS = 6
const val SLOTS = COLUMNS * ROWS

// Squelette : toutes les cases sont vides. Les bâtiments viendront de la base de données.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillageScreen(
    onEmptySlot: (Int) -> Unit,
    onBuilding: (Long) -> Unit,
    onSettings: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mon village") }) },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(COLUMNS),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Grass),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items((0 until SLOTS).toList()) { slot ->
                EmptySlot(onClick = { onEmptySlot(slot) })
            }
        }
    }
}

@Composable
private fun EmptySlot(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Ajouter un bâtiment")
    }
}
