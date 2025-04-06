package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(modifier = Modifier.padding(8.dp)) {
        val categories = listOf("All", "Politika", "Sport", "Nauka/tehnologija")
        categories.forEach { category ->
            AssistChip(
                onClick = { onCategorySelected(category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (category == selectedCategory) Color.Gray else Color.LightGray
                ),
                modifier = Modifier.padding(4.dp),
                label = { Text(text = category) }
            )
        }
    }
}