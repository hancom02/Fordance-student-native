package com.fordance.student.features.catalog.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.FordanceStroke
import com.fordance.student.core.theme.TextPlaceholder

@Composable
fun SearchInputBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search...",
    onFilterClick: (() -> Unit)? = null,
    hasActiveFilter: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = placeholder, color = TextPlaceholder)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = FordancePrimary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = TextPlaceholder
                    )
                }
            } else if (onFilterClick != null) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = if (hasActiveFilter) FordancePrimary else TextPlaceholder
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FordancePrimary,
            unfocusedBorderColor = FordanceStroke,
            cursorColor = FordancePrimary
        )
    )
}
