package com.bravedevelopers.bravepokemon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bravedevelopers.bravepokemon.realm.RealmRepo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Init list pokemons from database
        RealmRepo.instance.initProxyListPokemonsInFavorites(realm)

        setContentView(R.layout.activity_main)
        NavigationUI.setupWithNavController(bottomNavigation, findNavController(this@MainActivity, R.id.navHostMain))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this@MainActivity, R.id.navHostMain).navigateUp()

    override fun onPause() {
        //Update database with changes
        RealmRepo.instance.deleteOldAndAddNewProxiesFromToFavorites(realm)

        super.onPause()
    }

    override fun onDestroy() {
        //and close realm
        RealmRepo.instance.destroy()
        realm.close()

        super.onDestroy()
    }
}
