package com.syracuse.caloriemanager.views

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.syracuse.caloriemanager.databinding.FoodItemBinding
import com.syracuse.caloriemanager.models.FoodItem
import java.util.*
import kotlin.collections.ArrayList

class FoodAdapter(private val mList: ArrayList<FoodItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val initialFoodItems = ArrayList<FoodItem>().apply {
        mList?.let { addAll(it) }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val foodItem = mList[position]
        Log.v(TAG, "Adding to view: " + foodItem.name)
        when(holder) {
            is ViewHolder -> {
                with(holder) {
                    binding.name.text = foodItem.name
                    binding.description.text = foodItem.calories.toString() + " Calories, " + foodItem.fats + " Fats"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun getFilter(): Filter {
        return foodFilter
    }

    private val foodFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<FoodItem> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialFoodItems.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.getDefault())
                initialFoodItems.forEach {
                    if (it.name.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                mList.clear()
                mList.addAll(results.values as ArrayList<FoodItem>)
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    companion object {
        @JvmStatic val TAG = "FoodAdapter"
    }
}