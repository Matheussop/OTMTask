package com.dreamteam.tasksotm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.tasksotm.databinding.AddTaskActivityBinding

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: AddTaskActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddTaskActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            addTaskBt.setOnClickListener { addTask() }
        }
    }

    private fun addTask() {
        val taskName = binding.name.editText?.text
        val taskWorkload = binding.workload.editText?.text
        val taskPoints = binding.points.editText?.text

        if (!taskName.isNullOrEmpty() && !taskName.isNullOrEmpty()) {
            val intent = Intent()
            intent.putExtra("task_name", taskName.toString())
            intent.putExtra("task_workload", taskWorkload.toString())
            intent.putExtra("task_points", taskPoints.toString())
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "Campos preenchidos incorretamente", Toast.LENGTH_SHORT).show()
        }
    }
}