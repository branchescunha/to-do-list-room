package data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Tarefa::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tarefaDao(): TarefaDao
}