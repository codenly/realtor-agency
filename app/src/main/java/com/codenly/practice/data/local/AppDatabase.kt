package com.codenly.practice.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
        OwnerEntity::class,
        TypeEntity::class,
        ClientEntity::class,
        RealtorEntity::class,
        PropertyEntity::class,
        ViewEntity::class,
        DealEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getOwnerDao(): OwnerDao
    abstract fun getTypeDao(): TypeDao
    abstract fun getClientDao(): ClientDao
    abstract fun getRealtorDao(): RealtorDao
    abstract fun getPropertyDao(): PropertyDao
    abstract fun getViewDao(): ViewDao
    abstract fun getDealDao(): DealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
