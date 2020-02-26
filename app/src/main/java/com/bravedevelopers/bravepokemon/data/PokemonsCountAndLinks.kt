package com.bravedevelopers.bravepokemon.data

import com.google.gson.annotations.SerializedName

data class PokemonsCountAndLinks(
    @SerializedName("count") var count: Int = 0,
    @SerializedName("results") var pokeResults: List<PokeResults> = emptyList()
)
