package com.bravedevelopers.bravepokemon

import android.app.Application
import com.bravedevelopers.bravepokemon.data.Api
import com.google.gson.GsonBuilder
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {

    lateinit var api: Api

    override fun onCreate() {
        super.onCreate()

        //Create Gson with custom type adapter Prevent NPE with using model to realm
        val gson = GsonBuilder().registerTypeAdapterFactory(NullToEmptyAdapterFactory()).create()

        //Create global API instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)

        //init realm
        Realm.init(this)
    }
}

