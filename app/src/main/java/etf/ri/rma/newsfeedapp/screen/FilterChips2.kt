import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChips2(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        val categories = listOf("Datum ⇩", "Datum ⇧")
        val lista = listOf("sort_chip_date_asc","sort_chip_date_desc")

        categories.forEachIndexed { index, category ->
            val isSelected = category == selectedCategory
            AssistChip(
                onClick = { onCategorySelected(category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) Color.Gray else Color.LightGray
                ),
                border = BorderStroke(
                    width = Dp.Hairline,
                    color = if (isSelected) Color.DarkGray else Color.Gray
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .testTag(lista[index])
                    .semantics { selected = isSelected },
                label = { Text(text = category) },
            )
        }
    }
}