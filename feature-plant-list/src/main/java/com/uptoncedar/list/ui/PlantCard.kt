package com.uptoncedar.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.common.ui.FillingImage


@Composable
fun PlantCard(
    plantListEntry: PlantListEntry,
    onNavigateToDetails: (plantId: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onNavigateToDetails(plantListEntry.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Column(
                modifier = Modifier.width(120.dp)
            ) {
                FillingImage(plantListEntry=plantListEntry)
            }
            Column(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .fillMaxHeight()
            ) {
                VerticalDivider(
                    thickness = 5.dp, color = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                plantListEntry.common_name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                plantListEntry.scientific_name?.let {
                    Text(
                        text = it, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlantCardPreview() {
    MaterialTheme {
        PlantCard(
            plantListEntry = PlantListEntry(
                id = "123",
                common_name = "Rose",
                scientific_name = "Rosa spp.",
                image_url = "https://example.com/rose.jpg",
                author = "Timtohy Frisch",
                slug = "",
                status = "extinct",
                rank = "test",
                family = "rosa",
                genus = "rosacea",
                genus_id = "134095908sdicjqsudhq",
                links = Links(
                    self = "/api/v1/self", genus = "/api/v1/genus", plant = "/api/v1/plant"
                ),
                meta = Meta(
                    last_modified = "03-29-2025T12:00:00.00Z"
                )
            ), onNavigateToDetails = { /* Do nothing in preview */ })
    }
}