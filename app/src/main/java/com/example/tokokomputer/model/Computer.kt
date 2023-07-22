package com.example.tokokomputer.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "computer_table")
data class Computer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val telephone: String,
    val latitude: Double?,
    val longitude: Double?

    //nambah data latitud dan longitud disimpan dalam tabel
): Parcelable