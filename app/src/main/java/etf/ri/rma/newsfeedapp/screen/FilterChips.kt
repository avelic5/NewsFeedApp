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
fun FilterChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    FlowRow(modifier = Modifier.padding(8.dp)) {
        val chips = listOf(
            "all" to "filter_chip_all",
            "politics" to "filter_chip_pol",
            "sports" to "filter_chip_spo",
            "science" to "filter_chip_sci",
            "tech" to "filter_chip_tech",
            "music" to "filter_chip_none"
        )

        val labels = mapOf(
            "all" to "All",
            "politics" to "Politika",
            "sports" to "Sport",
            "science" to "Nauka/tehnologija",
            "tech" to "Nauka/tehnologija",
            "music" to "Muzika"
        )

        chips.forEach { (categoryKey, testTag) ->
            val isSelected = selectedCategory == categoryKey
            if(categoryKey!="tech") {
                AssistChip(
                    onClick = { onCategorySelected(categoryKey) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) Color.Gray else Color.LightGray
                    ),
                    border = BorderStroke(
                        width = Dp.Hairline,
                        color = if (isSelected) Color.DarkGray else Color.Gray
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .testTag(testTag)
                        .semantics { selected = isSelected },
                    label = { Text(labels[categoryKey] ?: categoryKey) }
                )
            }
        }
    }
}
