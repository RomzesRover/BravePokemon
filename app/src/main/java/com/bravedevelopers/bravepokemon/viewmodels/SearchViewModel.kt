package com.bravedevelopers.bravepokemon.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bravedevelopers.bravepokemon.data.Api
import com.bravedevelopers.bravepokemon.data.Pokemon
import com.bravedevelopers.bravepokemon.realm.RealmRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel: ViewModel() {
    val searchInProgress = MutableLiveData<Boolean>().apply { value = false }
    val nothingFound = MutableLiveData<Boolean>().apply { value = false }
    val loadedPokemon = MutableLiveData<Pokemon?>().apply { value = null }
    val pokemonInFavoritesList = MutableLiveData<Boolean>().apply { value = false }

    //api
    lateinit var api: Api
    private val disposable = CompositeDisposable()

    fun actionSearchStart(query: String){
        onLoadStart()
        disposable.add(
            api.getPokemonByIdOrName(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        onPokemonLoaded(it)
                    },
                    {
                        onErrorOccurred(it)
                    }
                )
        )
    }

    fun togglePokemonInFavorites(){
        loadedPokemon.value?.let {pokemon ->
            RealmRepo.instance.togglePokemonStatusInFavorites(pokemon)
            pokemonInFavoritesList.value = RealmRepo.instance.checkPokemonInFavoritesListById(pokemon.id)
        }
    }

    fun forceRecheckPokemonIsInFavoritesList(){
        loadedPokemon.value?.let { pokemon ->
            pokemonInFavoritesList.value = RealmRepo.instance.checkPokemonInFavoritesListById(pokemon.id)
        }
    }

    private fun onLoadStart(){
        searchInProgress.value = true
        nothingFound.value = false
        loadedPokemon.value = null
        pokemonInFavoritesList.value = false
    }

    private fun onPokemonLoaded(pokemon: Pokemon){
        if (pokemon.name.isNotBlank()) {
            loadedPokemon.value = pokemon
            searchInProgress.value = false
            nothingFound.value = false
            pokemonInFavoritesList.value = RealmRepo.instance.checkPokemonInFavoritesListById(pokemon.id)
        } else {
            throw Exception("Server returned empty result")
        }
    }

    private fun onErrorOccurred(throwable: Throwable){
        throwable.printStackTrace()
        searchInProgress.value = false
        nothingFound.value = true
        loadedPokemon.value = null
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}