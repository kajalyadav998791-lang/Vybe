package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfile::class,
        Note::class,
        Friend::class,
        ChatMessage::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VybeDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun friendDao(): FriendDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: VybeDatabase? = null

        fun getDatabase(context: Context): VybeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VybeDatabase::class.java,
                    "vybe_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
