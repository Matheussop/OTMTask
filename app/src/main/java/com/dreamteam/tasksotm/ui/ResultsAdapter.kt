package com.dreamteam.tasksotm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.tasksotm.R
import com.dreamteam.tasksotm.data.TaskModel
import com.dreamteam.tasksotm.databinding.ResultCardBinding
import com.dreamteam.tasksotm.databinding.TaskCardBinding


class ResultsDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel) = oldItem == newItem
    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel) = oldItem == newItem
}

class ResultsAdapter :
    ListAdapter<TaskModel, ResultsAdapter.ResultViewHolder>(ResultsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = ResultCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ResultViewHolder(private val view: ResultCardBinding) :
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
                suggestion.text = view.root.context.getString(R.string.suggestion_label).format(
                    (taskModel.workloadPercent * taskModel.workload),
                    (taskModel.workloadPercent * 100.0)
                ) + "%)"
                progress.progress = (taskModel.workloadPercent * 100).toInt()
            }
        }
    }
}