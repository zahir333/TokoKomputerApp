package com.example.tokokomputer.application

import android.app.Application
import com.example.tokokomputer.repository.ComputerRepository

class ComputerApp : Application() {
    val database by lazy { ComputerDatabase.getDatabase(this) }
    val repository by lazy { ComputerRepository(database.computerDao()) }
}