package data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefa")
data class Tarefa(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,

    val concluido: Boolean
)