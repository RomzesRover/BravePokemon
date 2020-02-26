package com.bravedevelopers.bravepokemon.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Pokemon(
    @SerializedName("id") @PrimaryKey var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("sprites") var sprites: PokemonSprites? = PokemonSprites()
): RealmObject()