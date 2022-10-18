package com.example.cryptonomicon.models

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromDescription(value: Description): String {
        val gson = Gson()
        return gson.toJson(value).toString()
    }

    @TypeConverter
    fun toDescription(value: String): Description {
        return Gson().fromJson(value, Description::class.java)
    }

    @TypeConverter
    fun fromTokenDetails(value: TokenDetails): String {
        val gson = Gson()
        return gson.toJson(value).toString()
    }

    @TypeConverter
    fun toTokenDetails(value: String): TokenDetails {
        return Gson().fromJson(value, TokenDetails::class.java)
    }

    @TypeConverter
    fun fromImageDetail(value: ImageDetail): String {
        val gson = Gson()
        return gson.toJson(value).toString()
    }

    @TypeConverter
    fun toTokenImageDetail(value: String): ImageDetail {
        return Gson().fromJson(value, ImageDetail::class.java)
    }

    @TypeConverter
    fun fromLinks(value: Links): String {
        val gson = Gson()
        return gson.toJson(value).toString()
    }

    @TypeConverter
    fun toLinks(value: String): Links {
        return Gson().fromJson(value, Links::class.java)
    }

}