package com.syracuse.caloriemanager.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.Query
import com.syracuse.caloriemanager.DairyFragment
import com.syracuse.caloriemanager.databinding.MealItemBinding
import com.syracuse.caloriemanager.models.MealItem

class MealAdapter(var modelClass: Class<MealItem>, var query: Query) :
    FirebaseRecyclerAdapter<MealItem, MealAdapter.MealViewHolder>(
        FirebaseRecyclerOptions.Builder<MealItem>()
            .setQuery(query, modelClass)
            .build()
    ){

    private var listener: ItemClickListener? = null
    private var isRemoveActive: Boolean = false

    interface ItemClickListener {
        fun onMealItemRemove(view: View)
    }

    fun setItemClickListener (listener: DairyFragment){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int, model: MealItem) {
        holder.binding.name.text = model.name
        holder.binding.unit.text = "${model.count} ${model.unit}"
        holder.binding.calories.text = model.calories.toString()

        if(isRemoveActive){
            holder.binding.removeMealItem.visibility = View.VISIBLE
        } else {
            holder.binding.removeMealItem.visibility = View.GONE
        }
    }

    fun setRemoveState(state: Boolean){
        this.isRemoveActive = state
        notifyDataSetChanged()
    }

    fun getRemoveState(): Boolean {
        return this.isRemoveActive
    }

    inner class MealViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.removeMealItem.setOnClickListener{view ->
                listener?.onMealItemRemove(view)
            }
        }
    }

    companion object {
        @JvmStatic val TAG = "MealAdapter"
    }

}