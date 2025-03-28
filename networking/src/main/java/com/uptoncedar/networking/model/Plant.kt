package com.uptoncedar.networking.model

data class Plant(
    val id: String,
    val author: String,
    val common_name: String,
    val slug: String,
    val scientific_name: String,
    val status: String,
    val rank: String,
    val family: String,
    val genus: String,
    val genus_id: String,
    val image_url: String,
    val links: Links,
    val meta: Meta
)

data class Links(
    val self: String,
    val genus: String,
    val plant: String
)

data class Meta(
    val last_modified: String
)