package com.example.androiddevchallenge.model

data class Puppy(
    val id: String,
    val name: String? = null,
    val images: List<String>? = null,
    val breed: String? = null,
    val age: String? = null,
    val gender: Gender? = null,
    val address: String? = null,
    val intro: String? = null,
    val gotVaccine: Boolean? = null,
    val sterilized: Boolean? = null
) {
    var like: Boolean = false
    enum class Gender {
        BOY, GIRL
    }
}