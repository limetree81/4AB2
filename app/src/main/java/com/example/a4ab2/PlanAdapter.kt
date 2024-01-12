package com.example.a4ab2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4ab2.databinding.ItemPlanBinding

class PlanAdapter(val datas: List<Plan>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class PlanViewHolder(val binding: ItemPlanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PlanViewHolder(ItemPlanBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as PlanViewHolder).binding
        binding.name.text = datas[position].name
        binding.repetition.text = datas[position].getSelectedDays()
        binding.firstDateTextView.text = datas[position].getFormattedFirstDate()

    }

    override fun getItemCount(): Int = datas.size
}