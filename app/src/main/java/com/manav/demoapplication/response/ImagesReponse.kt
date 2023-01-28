package com.manav.demoapplication.response

data class ImagesResponse(
    val code: Int,
    val data: List<Data>,
    val status: String,
    val total: Int
) {
    data class Data(
        val description: String,
        val title: String,
        val url: String
    )
}