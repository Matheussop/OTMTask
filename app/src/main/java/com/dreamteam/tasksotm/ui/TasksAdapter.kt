package com.dreamteam.tasksotm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.tasksotm.R
import com.dreamteam.tasksotm.data.TaskModel
import com.dreamteam.tasksotm.databinding.TaskCardBinding


class TasksDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel) = oldItem == newItem
    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel) = oldItem == newItem
}

class TasksAdapter(
    private val deleteCardListener: (itemId: Int) -> Unit
) : ListAdapter<TaskModel, TasksAdapter.TaskViewHolder>(TasksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = TaskCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class TaskViewHolder(private val view: TaskCardBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(taskModel: TaskModel) {
            view.apply {
                title.text = taskModel.name
                workload.text = view.root.context.getString(
                    R.string.workload_label,
                    taskModel.workload
                )
                points.text = view.root.context.getString(
                    R.string.points_label,
                    taskModel.points
                )

                closeIcon.setOnClickListener { deleteCardListener(taskModel.id) }
            }
        }
    }
}