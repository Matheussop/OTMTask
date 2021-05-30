package com.dreamteam.tasksotm.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dreamteam.tasksotm.R
import com.dreamteam.tasksotm.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding

    private var incompleteTasks = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        incompleteTasks = intent.extras?.getBoolean("incomplete_tasks") ?: true
        val timeAvailable = intent.extras?.getDouble("time_available") ?: 0.0

        Glide.with(this).load(R.raw.nyan_cat).into(binding.nyanCat)

        binding.incompleteTasksSwitch.apply {
            isChecked = incompleteTasks

            setOnCheckedChangeListener { _, isChecked ->
                incompleteTasks = isChecked
            }
        }

        binding.timeAvailable.editText?.setText(timeAvailable.toString())

        binding.saveBt.setOnClickListener { saveChanges() }
    }

    private fun saveChanges() {
        val intent = Intent()
        intent.putExtra("incomplete_tasks", incompleteTasks)
        intent.putExtra("time_available", binding.timeAvailable.editText?.text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }
}