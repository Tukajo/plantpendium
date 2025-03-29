package com.uptoncedar.networking.model

data class PlantDetails(
    val id: String,
    val author: String,
    val common_name: String,
    val slug: String,
    val scientific_name: String,
    val genus_id: String,
    val links: Links,
    val meta: Meta,
    val main_species_id: String,
    val main_species: Species
)