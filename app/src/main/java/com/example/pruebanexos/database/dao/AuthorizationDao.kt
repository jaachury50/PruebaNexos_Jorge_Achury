package com.example.pruebanexos.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pruebanexos.database.entities.AuthorizationEntity
import com.example.pruebanexos.models.AuthorizationResponseModel

@Dao
interface AuthorizationDao {

    @Query("SELECT * FROM authorizations_table ORDER BY CASE approvedStatus WHEN 1 THEN 0 ELSE 1 END, date DESC")
    fun getAllAuthorizations():List<AuthorizationEntity>

    @Query("SELECT * FROM authorizations_table WHERE approvedStatus == 1 ORDER BY date DESC")
    fun getAllAuthorizationsApproved():List<AuthorizationEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAuthorization(authorization: AuthorizationEntity): Long

    @Query("UPDATE authorizations_table SET approvedStatus = :approvedStatus WHERE receiptId = :receipId")
    fun deleteAuthorization(approvedStatus: Boolean,receipId: String): Int




}