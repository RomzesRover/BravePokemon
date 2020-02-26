package com.bravedevelopers.bravepokemon.data

import com.google.gson.annotations.SerializedName

data class PokeResults(
    @SerializedName("name") var name: String = "",
    @SerializedName("url") var url: String = ""
)
