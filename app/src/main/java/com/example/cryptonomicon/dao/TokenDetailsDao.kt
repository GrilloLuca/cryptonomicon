package com.example.cryptonomicon.dao

import androidx.room.*
import com.example.cryptonomicon.models.TokenDetails

@Dao
interface TokenDetailsDao {

    @Query("SELECT * FROM TokenDetails")
    fun getAll(): TokenDetails

    @Query("SELECT * FROM TokenDetails WHERE id = :id")
    fun findById(id: String?): TokenDetails

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg details: TokenDetails)

    @Delete
    fun delete(user: TokenDetails)

}