package com.bravedevelopers.bravepokemon

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter


@Suppress("UNCHECKED_CAST")
class NullToEmptyAdapterFactory: TypeAdapterFactory{
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        return when (type?.rawType as Class<T>){
            String::class.java -> StringAdapter() as TypeAdapter<T>
            Int::class.java -> IntAdapter() as TypeAdapter<T>
            else -> null
        }
    }
}

class StringAdapter: TypeAdapter<String>(){
    override fun write(writer: JsonWriter?, value: String?) {
        writer?.let {
            if (value == null){
                it.nullValue()
                return
            }
            it.value(value)
        }
    }

    override fun read(reader: JsonReader?): String? {
        reader?.let {
            if (it.peek() == JsonToken.NULL){
                it.nextNull()
                return ""
            }
            return it.nextString()
        }
        return null
    }
}

class IntAdapter: TypeAdapter<Int>(){
    override fun write(writer: JsonWriter?, value: Int?) {
        writer?.let {
            if (value == null){
                it.nullValue()
                return
            }
            it.value(value)
        }
    }

    override fun read(reader: JsonReader?): Int? {
        reader?.let {
            if (it.peek() == JsonToken.NULL){
                it.nextNull()
                return 0
            }
            return it.nextInt()
        }
        return null
    }
}