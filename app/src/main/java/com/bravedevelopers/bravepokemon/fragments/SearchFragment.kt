package com.bravedevelopers.bravepokemon.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bravedevelopers.bravepokemon.App
import com.bravedevelopers.bravepokemon.R
import com.bravedevelopers.bravepokemon.databinding.FragmentSearchBinding
import com.bravedevelopers.bravepokemon.viewmodels.SearchViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_an_pokemon.*
import java.util.*

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.api = (requireActivity().application as App).api
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forceRecheckPokemonIsInFavoritesList()

        btnActionSearchStart.setOnClickListener {
            viewModel.actionSearchStart(edTxSearchQuery.text.toString().toLowerCase(Locale.getDefault()))
        }

        edTxSearchQuery.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                viewModel.actionSearchStart(v.text.toString().toLowerCase(Locale.getDefault()))
            }
            false
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
