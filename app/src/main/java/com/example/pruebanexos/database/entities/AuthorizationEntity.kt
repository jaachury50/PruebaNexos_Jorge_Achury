package com.example.pruebanexos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "authorizations_table")
data class AuthorizationEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "receiptId")
    val receiptId: String,
    @ColumnInfo(name = "rrn")
    val rrn: String,
    @ColumnInfo(name = "commerceCode")
    val commerceCode: String,
    @ColumnInfo(name = "terminalCode")
    val terminalCode: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @ColumnInfo(name = "card")
    val card: String,
    @ColumnInfo(name = "statusCode")
    val statusCode: String,
    @ColumnInfo(name = "statusDescription")
    val statusDescription: String,
    @ColumnInfo(name = "approvedStatus")
    val approvedStatus: Boolean = true,
    @ColumnInfo(name = "date")
    val date: Date = Date()
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}