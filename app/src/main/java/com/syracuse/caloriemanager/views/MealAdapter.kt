package com.syracuse.caloriemanager.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syracuse.caloriemanager.databinding.MealItemBinding
import com.syracuse.caloriemanager.models.MealItem

class MealAdapter(private val mList: List<MealItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mealItem = mList[position]
        Log.v(TAG, "Adding to view: " + mealItem.name)
        when(holder) {
            is ViewHolder -> {
                with(holder) {
                    binding.mealItemName.text = mealItem.name
                    binding.mealItemUnit.text = mealItem.unit
                    binding.mealItemCalories.text = mealItem.calories.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    companion object {
        @JvmStatic val TAG = "MealAdapter"
    }
}