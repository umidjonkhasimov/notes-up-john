package uz.gita.notesup.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.gita.notesup.data.entities.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE in_trash = 0 ORDER BY id DESC")
    suspend fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNotesById(id: Int): Notes

    @Query("SELECT * FROM notes WHERE in_trash = 1 ORDER BY id DESC")
    suspend fun getAllTrashedNotes(): List<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: Notes)

    @Query("UPDATE notes SET in_trash = 1 WHERE id = :id")
    suspend fun moveToTrash(id: Int)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :title || '%' AND in_trash = 0")
    suspend fun search(title: String): List<Notes>

    @Query("UPDATE notes SET in_trash=0 WHERE id = :id")
    suspend fun restoreNote(id: Int)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("DELETE FROM notes WHERE in_trash = 1")
    suspend fun deleteAllTrashedNotes()
}