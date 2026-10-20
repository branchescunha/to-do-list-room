package data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TarefaDao {

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Query("SELECT * FROM tarefa")
    suspend fun listar(): List<Tarefa>

    @Query("SELECT * FROM tarefa WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Tarefa?

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)
}