package com.example.todolist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import data.AppDatabase
import data.Tarefa
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()

        val tarefaDao = db.tarefaDao()

        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextTarefa = findViewById<EditText>(R.id.editTextTarefa)

        val buttonAdicionar = findViewById<Button>(R.id.buttonAdicionar)
        val buttonBuscar = findViewById<Button>(R.id.buttonBuscar)
        val buttonAtualizar = findViewById<Button>(R.id.buttonAtualizar)
        val buttonDeletar = findViewById<Button>(R.id.buttonDeletar)

        val textViewResultado = findViewById<TextView>(R.id.textViewResultado)

        buttonAdicionar.setOnClickListener {

            val titulo = editTextTarefa.text.toString().trim()

            if (titulo.isNotEmpty()) {

                lifecycleScope.launch {

                    val tarefa = Tarefa(
                        titulo = titulo,
                        concluido = false
                    )

                    tarefaDao.inserir(tarefa)

                    editTextTarefa.text.clear()

                    Toast.makeText(
                        this@MainActivity,
                        "Tarefa salva!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                Toast.makeText(
                    this,
                    "Digite uma tarefa!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        buttonBuscar.setOnClickListener {

            lifecycleScope.launch {

                val tarefas = tarefaDao.listar()

                val resultado = tarefas.joinToString("\n") {
                    "${it.id} - ${it.titulo}"
                }

                textViewResultado.text = resultado
            }
        }

        buttonAtualizar.setOnClickListener {

            val id = editTextId.text.toString().toIntOrNull()
            val novoTitulo = editTextTarefa.text.toString().trim()

            if (id == null || novoTitulo.isEmpty()) {

                Toast.makeText(
                    this,
                    "Informe o ID e o novo título!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                lifecycleScope.launch {

                    val tarefaExistente = tarefaDao.buscarPorId(id)

                    if (tarefaExistente != null) {

                        val tarefaAtualizada = tarefaExistente.copy(
                            titulo = novoTitulo
                        )

                        tarefaDao.atualizar(tarefaAtualizada)

                        editTextId.text.clear()
                        editTextTarefa.text.clear()

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa atualizada!",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa não encontrada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        buttonDeletar.setOnClickListener {

            val id = editTextId.text.toString().toIntOrNull()

            if (id == null) {

                Toast.makeText(
                    this,
                    "Informe o ID da tarefa!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                lifecycleScope.launch {

                    val tarefa = tarefaDao.buscarPorId(id)

                    if (tarefa != null) {

                        tarefaDao.deletar(tarefa)

                        editTextId.text.clear()

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa deletada!",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa não encontrada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}