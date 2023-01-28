package com.manav.demoapplication.response

class PassengersData : ArrayList<Data>()

data class Data(
    val __v: Int,
    val _id: String,
    val name: String,
    val trips: Int
)
