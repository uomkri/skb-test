package ru.uomkri.skbtest.repo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RepoEntity::class], version = 2, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}