package com.manav.demoapplication.response

data class Companies(
    val code: Int,
    val data: ArrayList<Data.Addresse>,
    val status: String,
    val total: Int
) {
    data class Data(
        val addresses: List<Addresse>,
        val contact: Contact,
        val country: String,
        val email: String,
        val id: Int,
        val image: String,
        val name: String,
        val phone: String,
        val vat: String,
        val website: String
    ) {
        data class Addresse(
            val buildingNumber: String,
            val city: String,
            val country: String,
            val county_code: String,
            val id: Int,
            val latitude: Double,
            val longitude: Double,
            val street: String,
            val streetName: String,
            val zipcode: String
        )

        data class Address(
            val buildingNumber: String,
            val city: String,
            val country: String,
            val county_code: String,
            val id: Int,
            val latitude: Double,
            val longitude: Double,
            val street: String,
            val streetName: String,
            val zipcode: String
        )

        data class Contact(
            val address: Address,
            val birthday: String,
            val email: String,
            val firstname: String,
            val gender: String,
            val id: Int,
            val image: String,
            val lastname: String,
            val phone: String,
            val website: String
        )
    }
}