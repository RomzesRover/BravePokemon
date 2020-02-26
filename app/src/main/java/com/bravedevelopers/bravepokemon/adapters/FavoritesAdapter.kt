package com.bravedevelopers.bravepokemon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bravedevelopers.bravepokemon.R
import com.bravedevelopers.bravepokemon.data.Pokemon
import com.bravedevelopers.bravepokemon.realm.RealmRepo
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_an_pokemon.*


class FavoritesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pokemons: ArrayList<Pokemon> = ArrayList()

    fun setupPokemons(newPokemons: ArrayList<Pokemon>){
        pokemons.clear()
        pokemons.addAll(newPokemons)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.layout_an_pokemon, parent, false)
        return PokemonsViewHolder(itemView)
    }

    override fun getItemCount(): Int = pokemons.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PokemonsViewHolder){
            holder.setup(pokemons[position])
        }
    }

    class PokemonsViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer{
        fun setup(pokemon: Pokemon){
            chBxPokemonAddedToFavorites.isChecked = RealmRepo.instance.checkPokemonInFavoritesListById(pokemon.id)
            chBxPokemonAddedToFavorites.setOnClickListener {
                RealmRepo.instance.togglePokemonStatusInFavorites(pokemon)
            }
            txVwPokemonName.text = pokemon.name
            pokemon.sprites?.let { pokemonSprites ->
                if (pokemonSprites.frontDefault.isNotBlank())
                    Picasso.get().load(pokemonSprites.frontDefault).error(R.drawable.no_image).into(igVwPokemonAvatar)
                else
                    Picasso.get().load(R.drawable.no_image).into(igVwPokemonAvatar)
            }
        }
    }
}