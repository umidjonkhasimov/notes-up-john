package uz.gita.notesup.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
class Notes : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "sub_title")
    var subtitle: String? = null

    @ColumnInfo(name = "date_time")
    var dateTime: String? = null

    @ColumnInfo(name = "note_text")
    var noteText: String? = null

    @ColumnInfo(name = "img_path")
    var imgPath: String? = null

    @ColumnInfo(name = "web_link")
    var webLink: String? = null

    @ColumnInfo(name = "color")
    var color: String? = null

    @ColumnInfo(name = "in_trash")
    var inTrash: Int = 0

    override fun toString(): String {
        return "$title : $dateTime"
    }
}
