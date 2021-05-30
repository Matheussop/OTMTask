package com.dreamteam.tasksotm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreamteam.tasksotm.R
import com.dreamteam.tasksotm.data.TaskModel
import com.dreamteam.tasksotm.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SETTINGS_REQUEST_CODE = 1
        private const val ADD_TASK_REQUEST_CODE = 2
    }

    private lateinit var binding: ActivityMainBinding

    private val tasksAdapter = TasksAdapter(this::onItemRemoveClick)
    private val viewModel: MainViewModel by viewModels()

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tasksRecycler.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                adapter = tasksAdapter
            }

            configsBt.setOnClickListener { openSettings() }
            addTaskBt.setOnClickListener { addNewTask() }
            runSolverBt.setOnClickListener { runSolver() }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        viewModel.tasksList.observe(this, { tasksList ->
            tasksAdapter.submitList(tasksList) {
                if (tasksList.isEmpty())
                    binding.emptyListMsg.visibility = View.VISIBLE
                else
                    binding.emptyListMsg.visibility = View.GONE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SETTINGS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val incompleteTasks =
                        data?.getBooleanExtra("incomplete_tasks", viewModel.incompleteTasks)

                    val timeAvailable = data?.getStringExtra("time_available")

                    if (incompleteTasks != null)
                        viewModel.incompleteTasks = incompleteTasks

                    if (timeAvailable != null) {
                        timeAvailable.toDoubleOrNull()?.let {
                            viewModel.timeAvailable = it
                        }
                    }
                }
            }
            ADD_TASK_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val taskName = data?.getStringExtra("task_name")
                    val taskWorkload = data?.getStringExtra("task_workload")?.toDoubleOrNull()
                    val taskPoints = data?.getStringExtra("task_points")?.toDoubleOrNull()

                    if (taskName != null && taskWorkload != null && taskPoints != null)
                        viewModel.addTask(taskName, taskWorkload, taskPoints)
                }
            }
        }
    }

    private fun onItemRemoveClick(itemId: Int) = viewModel.removeTask(itemId)

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("incomplete_tasks", viewModel.incompleteTasks)
        intent.putExtra("time_available", viewModel.timeAvailable)
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }

    private fun addNewTask() {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
    }

    private fun runSolver() {
        if (!viewModel.tasksList.value.isNullOrEmpty()) {
            scope.launch {
                withContext(Dispatchers.Main) {
                    binding.runSolverBt.apply {
                        setImageDrawable(getDrawable(R.drawable.ic_sync))
                        binding.runSolverBt.isClickable = false
                    }
                }

                viewModel.runSolver(
                    onSuccess = {
                        showResults(it)
                    },
                    onError = {
                        Toast.makeText(this@MainActivity, "Erro no servidor :(", Toast.LENGTH_SHORT)
                            .show()
                    })

                withContext(Dispatchers.Main) {
                    binding.runSolverBt.apply {
                        setImageDrawable(getDrawable(R.drawable.ic_play))
                        binding.runSolverBt.isClickable = true
                    }
                }
            }
        } else {
            Toast.makeText(this, "Adicione tasks!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResults(response: List<TaskModel>) {
        val data = Gson().toJson(response)
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("results", data)
        intent.putExtra("time", viewModel.timeAvailable)
        intent.putExtra("available", viewModel.incompleteTasks)
        startActivity(intent)
    }
}