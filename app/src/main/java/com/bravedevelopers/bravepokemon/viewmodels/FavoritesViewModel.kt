package com.bravedevelopers.bravepokemon.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bravedevelopers.bravepokemon.data.Pokemon
import com.bravedevelopers.bravepokemon.realm.RealmRepo

class FavoritesViewModel: ViewModel() {
    val favoritesPokemonList = MutableLiveData<ArrayList<Pokemon>>()
    val nothingFound = MutableLiveData<Boolean>().apply { value = true }

    fun fetchFavoritesPokemonList(){
        val listFromDatabase = RealmRepo.instance.listPokemonsInFavorites
        favoritesPokemonList.value = listFromDatabase
        nothingFound.value = listFromDatabase.isEmpty()
    }
}