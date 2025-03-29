package com.uptoncedar.common.model


data class Species(
    val status: String,
    val rank: String,
    val family: String,
    val genus: String,
    val image_url: String,
    val specifications: Map<String, Any>,
    val growth: Map<String, Any>,
    val images: Images,
    val common_names: List<CommonNames>
)

data class Images(
    val other: List<OtherImage>
)

data class OtherImage(
    val id: String,
    val image_url: String,
    val copyright: String
)

data class CommonNames(
    val OTHER: List<String>?,
    val ENGLISH: List<String>?
)