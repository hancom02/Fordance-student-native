package com.fordance.student.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Session
import com.fordance.student.core.theme.DarkOverlay
import com.fordance.student.core.theme.FordancePrimary
import com.fordance.student.core.theme.LevelAdvanced
import com.fordance.student.core.theme.LevelBeginner
import com.fordance.student.core.theme.LevelIntermediate
import com.fordance.student.core.theme.TextPrimary
import com.fordance.student.core.theme.TextSecondary

@Composable
fun LevelBadge(
    level: DanceLevel,
    modifier: Modifier = Modifier
) {
    val bgColor = when (level) {
        DanceLevel.BEGINNER -> LevelBeginner
        DanceLevel.INTERMEDIATE -> LevelIntermediate
        DanceLevel.ADVANCED -> LevelAdvanced
    }
    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.displayName.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun GenreBadge(
    genre: String,
    modifier: Modifier = Modifier
) {
    if (genre.isBlank()) return
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 11.sp
            )
        )
    }
}

/**
 * Large horizontal card used in Discover carousels (Featured, Joined, Favourite)
 */
@Composable
fun SessionHorizontalCard(
    session: Session,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(320.dp)
            .height(190.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = session.thumbnailUrl,
                contentDescription = session.sessionName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient scrim overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkOverlay.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LevelBadge(level = session.level)
                    GenreBadge(genre = session.genre)
                }

                Column {
                    Text(
                        text = session.sessionName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 20.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!session.instructorName.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = session.instructorName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * List item card used in Catalog Screen (Sessions tab)
 */
@Composable
fun SessionCard(
    session: Session,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = session.thumbnailUrl,
                contentDescription = session.sessionName,
                modifier = Modifier
                    .size(width = 110.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LevelBadge(level = session.level)
                    GenreBadge(genre = session.genre)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = session.sessionName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!session.instructorName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = session.instructorName,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (session.price > 0) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$${session.price}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FordancePrimary
                        )
                    )
                }
            }
        }
    }
}
