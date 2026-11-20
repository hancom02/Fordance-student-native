package com.fordance.student.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fordance.student.core.components.FordancePrimaryButton
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.TextPrimary
import com.fordance.student.core.theme.TextSecondary
import com.fordance.student.features.auth.presentation.AuthUiEvent
import com.fordance.student.features.auth.presentation.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser = authViewModel.uiState.value.currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Avatar circle
        Column(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(FordancePrimary.copy(alpha = 0.15f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = FordancePrimary,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentUser?.username ?: "Student User",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = currentUser?.email ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(32.dp))

        FordancePrimaryButton(
            text = "Sign Out",
            onClick = {
                authViewModel.onEvent(AuthUiEvent.SignOutRequested)
                onSignOut()
            },
            modifier = Modifier.width(200.dp)
        )
    }
}
