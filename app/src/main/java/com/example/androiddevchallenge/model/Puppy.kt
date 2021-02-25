package com.example.androiddevchallenge.model

data class Puppy(
    val id: String,
    val name: String? = null,
    val images: List<String>? = null,
    val breed: String? = null,
    val age: Int? = null,
    val gender: Gender? = null,
    val birthday: String? = null,
    val intro: String? = null,
    val gotVaccine: Boolean? = null,
    val sterilized: Boolean? = null
) {
    enum class Gender {
        BOY, GIRL
    }
}