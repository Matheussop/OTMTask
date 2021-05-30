package com.dreamteam.tasksotm.ui

import android.os.Bundle
import android.provider.MediaStore.Video
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreamteam.tasksotm.data.TaskModel
import com.dreamteam.tasksotm.databinding.ResultsActivityBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ResultsActivityBinding

    private val resultsAdapter = ResultsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: String = intent.extras?.getString("results")
            ?: run {
                Toast.makeText(
                    this,
                    "Ocorreu um erro :(",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@run ""
            }
        val type = object : TypeToken<List<TaskModel>>() {}.type
        val list = Gson().fromJson<List<TaskModel>>(data, type)

        val time = intent.extras?.getDouble("time", 0.0)
        val available = intent.extras?.getBoolean("available", true)

        binding.time.text = "Tempo disponível: " + time.toString() + " horas"
        binding.available.text = "Tarefas incompletas: " + available.toString()

        binding.points.text =
            "Pontuação máxima calculada: " + String.format("%.2f", calcPoints(list)) + " pontos"

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(
                this@ResultsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = resultsAdapter
        }
        resultsAdapter.submitList(list)
    }

    private fun calcPoints(list: List<TaskModel>): Double {
        var sum = 0.0

        list.forEach {
            sum += (it.points * it.workloadPercent)
        }
        return sum
    }
}