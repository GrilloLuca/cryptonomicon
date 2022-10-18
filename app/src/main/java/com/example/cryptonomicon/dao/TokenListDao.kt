package com.example.cryptonomicon.dao

import androidx.room.*
import com.example.cryptonomicon.models.Token

@Dao
interface TokenListDao {

    @Query("SELECT * FROM Token")
    fun getAll(): List<Token>

    @Query("SELECT * FROM Token WHERE id = :id")
    fun findById(id: String?): Token

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg details: Token)

    @Delete
    fun delete(user: Token)

}