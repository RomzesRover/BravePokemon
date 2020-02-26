package com.bravedevelopers.bravepokemon.realm

import com.bravedevelopers.bravepokemon.data.Pokemon
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmRepo private constructor(){

    fun destroy(){
        ourInstance = null
    }

    companion object {
        private var ourInstance: RealmRepo? = null

        val instance: RealmRepo
            get() {
                if (ourInstance == null)
                    ourInstance = RealmRepo()
                return ourInstance!!
            }
    }

    val listPokemonsInFavorites: ArrayList<Pokemon> = ArrayList()

    private lateinit var proxyListPokemonsInFavorites: RealmResults<Pokemon>
    private val proxyListPokemonsIdsToDeleteFromFavorites: ArrayList<Int> = ArrayList()
    private val proxyListPokemonsToAddToFavorites: ArrayList<Pokemon> = ArrayList()

    fun initProxyListPokemonsInFavorites(realm: Realm){
        realm.executeTransaction {
            proxyListPokemonsInFavorites = it.where<Pokemon>().findAll()
        }
        listPokemonsInFavorites.clear()
        listPokemonsInFavorites.addAll(proxyListPokemonsInFavorites)
    }

    fun deleteOldAndAddNewProxiesFromToFavorites(realm: Realm, updateListPokemonsInFavorites: Boolean = false){
        realm.executeTransaction {
            proxyListPokemonsToAddToFavorites.forEach { pokemon ->
                it.copyToRealmOrUpdate(pokemon)
            }
            proxyListPokemonsIdsToDeleteFromFavorites.forEach { id ->
                realm.where<Pokemon>().equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
        proxyListPokemonsToAddToFavorites.clear()
        proxyListPokemonsIdsToDeleteFromFavorites.clear()
        if (updateListPokemonsInFavorites)
            initProxyListPokemonsInFavorites(realm)
    }

    fun togglePokemonStatusInFavorites(pokemonToToggle: Pokemon){
        if (proxyListPokemonsInFavorites.any { pokemon -> pokemon.id == pokemonToToggle.id }){
            if (proxyListPokemonsIdsToDeleteFromFavorites.any { id -> id == pokemonToToggle.id }){
                listPokemonsInFavorites.add(pokemonToToggle)
                proxyListPokemonsIdsToDeleteFromFavorites.removeAll { id -> id == pokemonToToggle.id }
            } else {
                listPokemonsInFavorites.removeAll { pokemon -> pokemon.id == pokemonToToggle.id }
                proxyListPokemonsIdsToDeleteFromFavorites.add(pokemonToToggle.id)
            }
        } else {
            if (proxyListPokemonsToAddToFavorites.any { pokemon -> pokemon.id == pokemonToToggle.id }) {
                listPokemonsInFavorites.removeAll { pokemon -> pokemon.id == pokemonToToggle.id }
                proxyListPokemonsToAddToFavorites.removeAll { pokemon -> pokemon.id == pokemonToToggle.id }
            } else {
                listPokemonsInFavorites.add(pokemonToToggle)
                proxyListPokemonsToAddToFavorites.add(pokemonToToggle)
            }
        }
    }

    fun checkPokemonInFavoritesListById(id: Int): Boolean{
        return listPokemonsInFavorites.any { pokemon -> pokemon.id == id }
    }
}