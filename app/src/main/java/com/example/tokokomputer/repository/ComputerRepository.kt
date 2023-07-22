package com.example.tokokomputer.repository

import com.example.tokokomputer.dao.ComputerDao
import com.example.tokokomputer.model.Computer
import kotlinx.coroutines.flow.Flow

class ComputerRepository(private val computerDao: ComputerDao) {
    val allComputer: Flow<List<Computer>> = computerDao.getALLComputer()

    suspend fun insertComputer(computer: Computer) {
        computerDao.insertComputer(computer)
    }
    suspend fun deleteComputer(computer: Computer) {
        computerDao.deleteComputer(computer)
    }

    suspend fun updateComputer(computer: Computer) {
        computerDao.updateComputer(computer)
    }
}