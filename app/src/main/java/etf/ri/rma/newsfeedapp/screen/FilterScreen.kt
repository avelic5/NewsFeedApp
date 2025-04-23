package etf.ri.rma.newsfeedapp.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController, onApplyFilters: (String, String, List<String>) -> Unit) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("Politika", "Sport", "Nauka/tehnologija", "All")
    val categoryTags = listOf("filter_chip_pol", "filter_chip_spo", "filter_chip_sci", "filter_chip_all")

    var dateRange by remember { mutableStateOf("Odaberite opseg datuma") }
    var showDatePicker by remember { mutableStateOf(false) }

    var unwantedWord by remember { mutableStateOf("") }
    var unwantedWords by remember { mutableStateOf(listOf<String>()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category Chips
            Text("Kategorije", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEachIndexed { index, category ->
                    AssistChip(
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.testTag(categoryTags[index])
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Range Picker
            Text("Opseg datuma", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = dateRange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("filter_daterange_display"),
                    style = MaterialTheme.typography.bodySmall // Use a smaller typography style
                )
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.testTag("filter_daterange_button")
                ) {
                    Text("Odaberite")
                }
            }

            if (showDatePicker) {
                val datePickerState = rememberDateRangePickerState()

                Dialog(onDismissRequest = { showDatePicker = false }) {
                    Surface {
                        DateRangePicker(
                            state = datePickerState,
                            modifier = Modifier.padding(16.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                            TextButton(onClick = {
                                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val startDate = datePickerState.selectedStartDateMillis?.let { Date(it) }
                                val endDate = datePickerState.selectedEndDateMillis?.let { Date(it) }

                                if (startDate != null && endDate != null) {
                                    dateRange = "${formatter.format(startDate)} - ${formatter.format(endDate)}"
                                }
                                showDatePicker = false
                            }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unwanted Words Filter
            Text("Nepoželjne riječi", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = unwantedWord,
                    onValueChange = { unwantedWord = it },
                    placeholder = { Text("Unesite riječ") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("filter_unwanted_input"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Button(
                    onClick = {
                        val word = unwantedWord.trim()
                        if (word.isNotEmpty() && unwantedWords.none { it.equals(word, ignoreCase = true) }) {
                            unwantedWords = unwantedWords + word
                        }
                        unwantedWord = ""
                    },
                    modifier = Modifier.testTag("filter_unwanted_add_button")
                ) {
                    Text("Dodaj")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.testTag("filter_unwanted_list")) {
                items(unwantedWords.size) { index ->
                    Text(unwantedWords[index], style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Apply Filters Button
            Button(
                onClick = {
                    val formattedDateRange = if (dateRange != "Odaberite opseg datuma") {
                        dateRange
                    } else {
                        ""
                    }
                    onApplyFilters(selectedCategory, formattedDateRange, unwantedWords)
                    navController.popBackStack() // Navigate back to NewsFeedScreen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_apply_button")
            ) {
                Text("Primijeni filtere")
            }
        }
    }
}