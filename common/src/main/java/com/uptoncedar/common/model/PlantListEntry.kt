package com.uptoncedar.common.model

data class PlantListEntry(
    val id: String,
    val author: String,
    val common_name: String?,
    val slug: String,
    val scientific_name: String?,
    val status: String,
    val rank: String,
    val family: String,
    val genus: String,
    val genus_id: String,
    val image_url: String,
    val links: Links,
    val meta: Meta
)