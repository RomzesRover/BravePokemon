package com.bravedevelopers.bravepokemon.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bravedevelopers.bravepokemon.adapters.FavoritesAdapter
import com.bravedevelopers.bravepokemon.databinding.FragmentFavoritesBinding
import com.bravedevelopers.bravepokemon.viewmodels.FavoritesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.favoritesViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup list
        adapter = FavoritesAdapter()
        rvFavoritesList.adapter = adapter
        rvFavoritesList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvFavoritesList.setHasFixedSize(true)

        viewModel.favoritesPokemonList.observe(viewLifecycleOwner, Observer {
            adapter.setupPokemons(it)
        })
    }

    override fun onResume() {
        //list to display should be updated on Resume, cuz we remove obj from realm on Pause
        viewModel.fetchFavoritesPokemonList()
        super.onResume()
    }
}
