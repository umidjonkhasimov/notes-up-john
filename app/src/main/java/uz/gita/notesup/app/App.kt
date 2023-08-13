package uz.gita.notesup.app

import android.app.Application
import uz.gita.notesup.data.database.NotesDatabase

class App : Application() {

    override fun onCreate() {
        NotesDatabase.init(this)
        super.onCreate()
    }
}