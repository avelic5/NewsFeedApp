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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        val categories = listOf("All", "Politika", "Sport", "Nauka/tehnologija", "Muzika")
        val lista = listOf("filter_chip_all", "filter_chip_pol", "filter_chip_spo", "filter_chip_sci", "filter_chip_none")

        categories.forEachIndexed { index, category ->
            AssistChip(
                onClick = { onCategorySelected(category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (category == selectedCategory) Color.Gray else Color.LightGray
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .testTag(lista[index]),
                label = { Text(text = category) }
            )
        }
    }
}