package com.bravedevelopers.bravepokemon.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("/api/v2/pokemon/{idOrName}/")
    fun getPokemonByIdOrName(@Path("idOrName") idOrName: String): Observable<Pokemon>

    @GET("/api/v2/pokemon/?offset=0&limit=-1")
    fun getAllPokemonsCountAndLinks(): Observable<PokemonsCountAndLinks>
}