package uz.gita.notesup.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.notesup.data.dao.NotesDao
import uz.gita.notesup.data.entities.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getDao(): NotesDao

    companion object {
        private lateinit var instance: NotesDatabase

        @Synchronized
        fun init(context: Context) {
            if (!(Companion::instance.isInitialized)) {
                instance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "notes.db"
                ).build()
            }
        }

        fun getDatabase(): NotesDatabase = instance
    }
}