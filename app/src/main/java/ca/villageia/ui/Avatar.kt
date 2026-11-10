package ca.villageia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val faces = listOf(
    "👩", "👨", "🧑", "👵", "👴", "🧔", "👩‍🦰", "👨‍🦱", "🧑‍🦳", "👱‍♀️",
    "👱", "👩‍🦱", "👨‍🦰", "🧑‍🦲", "👩‍🦳", "👧", "👦", "🧓",
)
private val colors = listOf(
    0xFFFFCDD2, 0xFFF8BBD0, 0xFFE1BEE7, 0xFFC5CAE9, 0xFFBBDEFB,
    0xFFB2EBF2, 0xFFC8E6C9, 0xFFFFF9C4, 0xFFFFE0B2, 0xFFD7CCC8,
)

/** Avatar provisoire : visage et couleur tirés de la graine. */
@Composable
fun Avatar(seed: String, size: Dp = 48.dp, modifier: Modifier = Modifier) {
    val h = seed.hashCode() and Int.MAX_VALUE
    Box(
        modifier.size(size).clip(CircleShape).background(Color(colors[(h / 7) % colors.size])),
        contentAlignment = Alignment.Center,
    ) { Text(faces[h % faces.size], fontSize = (size.value * 0.55f).sp) }
}
