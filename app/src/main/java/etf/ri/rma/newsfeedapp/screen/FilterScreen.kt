package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    navController: NavController,
    selectedCategory: String,
    dateRange: String,
    unwantedWords: List<String>,
    onApplyFilters: (String, String, List<String>) -> Unit
) {
    var currentCategory by remember { mutableStateOf(selectedCategory) }
    var currentDateRange by remember { mutableStateOf(if (dateRange.isEmpty()) "Svi datumi" else dateRange) }
    var currentUnwantedWords by remember { mutableStateOf(unwantedWords) }
    var unwantedWord by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category Chips
            Text(
                "KATEGORIJE:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val categories = listOf("All", "Politika", "Sport", "Nauka/tehnologija","Muzika")
            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { category ->
                    val testTag = when (category) {
                        "Politika" -> "filter_chip_pol"
                        "Sport" -> "filter_chip_spo"
                        "Nauka/tehnologija" -> "filter_chip_sci"
                        "All" -> "filter_chip_all"
                        "Muzika" -> "filter_chip_none"
                        else -> ""
                    }
                    AssistChip(
                        onClick = { currentCategory = category },
                        label = { Text(category) },
                        modifier = Modifier
                            .testTag(testTag)
                            .semantics { selected = currentCategory == category },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (currentCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            labelColor = if (currentCategory == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Range Picker
            Text(
                "Izaberite opseg datuma:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentDateRange,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f).testTag("filter_daterange_display")
                )
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier
                        .testTag("filter_daterange_button")
                        .padding(start = 8.dp)
                ) {
                    Text("Odaberite", fontSize = 14.sp)
                }
            }

            if (showDatePicker) {
                DateRangePickerModal(
                    onDismiss = { showDatePicker = false },
                    onDateRangeSelected = { startDate, endDate ->
                        currentDateRange = if (startDate != null && endDate != null) {
                            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            "${formatter.format(Date(startDate))};${formatter.format(Date(endDate))}"
                        } else {
                            "Svi datumi" // Default value
                        }
                        showDatePicker = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unwanted Words Filter
            Text(
                "Nepoželjne riječi:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
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
                        .testTag("filter_unwanted_input")
                )
                Button(
                    onClick = {
                        val word = unwantedWord.trim()
                        if (word.isNotEmpty() && currentUnwantedWords.none { it == word }) {
                            currentUnwantedWords = currentUnwantedWords + word
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
                items(currentUnwantedWords.size) { index ->
                    Text(currentUnwantedWords[index], style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Apply Filters Button
            Button(
                onClick = {
                    onApplyFilters(currentCategory, currentDateRange, currentUnwantedWords)
                    navController.popBackStack()
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDismiss: () -> Unit,
    onDateRangeSelected: (Long?, Long?) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Poništi")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Izaberite opseg datuma",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            headline = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateRangePickerState.selectedStartDateMillis?.let {
                            SimpleDateFormat("d.MMM.yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Datum početka",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = dateRangePickerState.selectedEndDateMillis?.let {
                            SimpleDateFormat("d.MMM.yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Datum završetka",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        maxLines = 1,
                        textAlign = TextAlign.End
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(10.dp)
        )
    }
}