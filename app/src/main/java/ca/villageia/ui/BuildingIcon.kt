package ca.villageia.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import ca.villageia.catalog.BuildingType
import ca.villageia.catalog.GarageArt

@Composable
fun BuildingIcon(type: BuildingType?, size: Dp, modifier: Modifier = Modifier) {
    val art = type?.art
    if (art != null) GarageDrawing(art, modifier.size(size))
    else Text(type?.emoji ?: "🖼️", fontSize = (size.value * 0.8f).sp, modifier = modifier)
}

@Composable
private fun GarageDrawing(art: GarageArt, modifier: Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawRect(Color(art.wall), Offset(w * 0.08f, h * 0.38f), Size(w * 0.84f, h * 0.56f))
        if (art.pitched) {
            val roof = Path().apply {
                moveTo(w * 0.02f, h * 0.40f); lineTo(w * 0.5f, h * 0.08f); lineTo(w * 0.98f, h * 0.40f); close()
            }
            drawPath(roof, Color(art.roof))
        } else {
            drawRect(Color(art.roof), Offset(w * 0.04f, h * 0.28f), Size(w * 0.92f, h * 0.14f))
        }
        val dx = w * 0.18f; val dy = h * 0.50f; val dw = w * 0.64f; val dh = h * 0.44f
        drawRect(Color(0xFFECEFF1), Offset(dx, dy), Size(dw, dh))
        for (i in 1..3) {
            drawLine(Color(0xFF90A4AE), Offset(dx, dy + dh * i / 4f), Offset(dx + dw, dy + dh * i / 4f), strokeWidth = h * 0.02f)
        }
        drawRect(Color(0xFF607D8B), Offset(dx, dy), Size(dw, dh), style = Stroke(w * 0.03f))
    }
}
