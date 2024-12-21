package com.cableguy.volunteer_app.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cableguy.volunteer_app.room.dao.TravellerDao
import com.cableguy.volunteer_app.room.dao.TripDao
import com.cableguy.volunteer_app.room.entity.Traveller
import com.cableguy.volunteer_app.room.entity.TravellerTripCrossRef
import com.cableguy.volunteer_app.room.entity.Trip

@Database(entities = [Traveller::class, Trip::class, TravellerTripCrossRef::class], version = 3, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun travellerDao(): TravellerDao
    abstract fun travellerTripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new "trips" table
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `trips` (`tripId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `destination` TEXT NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT NOT NULL, `description` TEXT NOT NULL)"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new "traveller_trip_cross_ref" table
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `traveller_trip_cross_ref` (`travellerId` INTEGER NOT NULL, `tripId` INTEGER NOT NULL, PRIMARY KEY(`travellerId`, `tripId`), FOREIGN KEY(`travellerId`) REFERENCES `travellers`(`travellerId`) ON DELETE CASCADE, FOREIGN KEY(`tripId`) REFERENCES `trips`(`tripId`) ON DELETE CASCADE)"
                )
            }
        }

        fun getDatabase(context: android.content.Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "local_database"
                )
//                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add all migrations here
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}