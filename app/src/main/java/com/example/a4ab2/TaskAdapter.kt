package com.example.a4ab2

import Task
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4ab2.databinding.ItemTaskBinding

class TaskAdapter(val datas: List<Task>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class TaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as TaskViewHolder).binding
        binding.name.text = datas[position].plan.name
        binding.check.isChecked = datas[position].done
    }

    override fun getItemCount(): Int = datas.size
}