package com.fordance.student.features.catalog.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fordance.student.core.components.FordancePrimaryButton
import com.fordance.student.core.components.TagChip
import com.fordance.student.core.domain.model.CatalogFilter
import com.fordance.student.core.domain.model.DanceGenre
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    initialFilter: CatalogFilter,
    instructors: List<Instructor>,
    onDismissRequest: () -> Unit,
    onApplyFilter: (CatalogFilter) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var tempFilter by remember(initialFilter) { mutableStateOf(initialFilter) }
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Reset",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = FordancePrimary
                    ),
                    modifier = Modifier.clickable {
                        tempFilter = CatalogFilter()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Level Section
            Text(
                text = "Dance Level",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DanceLevel.entries.forEach { level ->
                    val isSelected = tempFilter.levels.contains(level)
                    TagChip(
                        text = level.displayName,
                        selected = isSelected,
                        onClick = {
                            tempFilter = tempFilter.toggleLevel(level)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Genre Section
            Text(
                text = "Dance Genre",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DanceGenre.entries.forEach { genre ->
                    val isSelected = tempFilter.genres.contains(genre.rawValue)
                    TagChip(
                        text = genre.displayName,
                        selected = isSelected,
                        onClick = {
                            tempFilter = tempFilter.toggleGenre(genre.rawValue)
                        }
                    )
                }
            }

            // Instructors Section
            if (instructors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Instructors",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    instructors.forEach { instructor ->
                        val isSelected = tempFilter.selectedInstructorIds.contains(instructor.id)
                        TagChip(
                            text = instructor.name,
                            selected = isSelected,
                            onClick = {
                                tempFilter = tempFilter.toggleInstructor(instructor.id)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Apply Button
            FordancePrimaryButton(
                text = "Apply Filters",
                onClick = {
                    onApplyFilter(tempFilter)
                    onDismissRequest()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
