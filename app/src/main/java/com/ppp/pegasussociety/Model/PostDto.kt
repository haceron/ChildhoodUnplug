package com.ppp.pegasussociety.Model

data class PostDto(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String,
    val warningInfo: Boolean,
    val attachmentUrl: String,
    val tagAge: String,
    val tagSeason: String,
    val tagActivityTime: Int,
    val tagInterest: List<String>,
    val tagDaytime: List<String>,
    val tagType: List<String>,
    val createdAt: String
)
