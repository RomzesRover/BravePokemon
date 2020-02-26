package com.bravedevelopers.bravepokemon.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bravedevelopers.bravepokemon.App
import com.bravedevelopers.bravepokemon.R
import com.bravedevelopers.bravepokemon.databinding.FragmentRandomBinding
import com.bravedevelopers.bravepokemon.viewmodels.RandomViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_random.*
import kotlinx.android.synthetic.main.layout_an_pokemon.*

class RandomFragment : Fragment() {

    private val viewModel: RandomViewModel by activityViewModels()
    private lateinit var binding: FragmentRandomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRandomBinding.inflate(inflater, container, false)
        binding.randomViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.api = (requireActivity().application as App).api
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forceRecheckPokemonIsInFavoritesList()

        btnActionRandomStart.setOnClickListener {
            viewModel.actionRandomStart()
        }

        chBxPokemonAddedToFavorites.setOnClickListener {
            viewModel.togglePokemonInFavorites()
        }

        viewModel.pokemonInFavoritesList.observe(viewLifecycleOwner, Observer {
            chBxPokemonAddedToFavorites.isChecked = it
        })

        viewModel.loadedPokemon.observe(viewLifecycleOwner, Observer {
            it?.let {
                txVwPokemonName.text = it.name
                it.sprites?.let {pokemonSprites ->
                    if (pokemonSprites.frontDefault.isNotBlank())
                        Picasso.get().load(pokemonSprites.frontDefault).error(R.drawable.no_image).into(igVwPokemonAvatar)
                    else
                        Picasso.get().load(R.drawable.no_image).into(igVwPokemonAvatar)
                }
            }
        })
    }
}
