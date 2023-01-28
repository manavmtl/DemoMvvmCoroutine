package com.core.core.response

class AllAirlines : ArrayList<AllAirlinesItem>()

data class AllAirlinesItem(
    val country: String,
    val established: String,
    val head_quaters: String,
    val id: Any,
    val logo: String,
    val name: String,
    val slogan: String,
    val website: String
)