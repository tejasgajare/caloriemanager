package com.syracuse.caloriemanager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.syracuse.caloriemanager.databinding.ActivityFoodBinding
import com.syracuse.caloriemanager.models.FoodItem
import com.syracuse.caloriemanager.models.MealItem
import com.syracuse.caloriemanager.views.FoodAdapter
import androidx.appcompat.widget.SearchView

class FoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodBinding
    private lateinit var mFoodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        this.title = ""
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val extras = intent.extras
        if (extras != null) {
            when(extras.getString("type")){
                resources.getString(R.string.meal_breakfast) -> {
                    binding.title.text = resources.getString(R.string.meal_breakfast)
                }
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val foods = ArrayList<FoodItem>()
        foods.add(FoodItem("Chicken Sandwich", "1 slice", 210, 23, 34))
        foods.add(FoodItem("Veggie Sandwich", "1 slice", 210, 23, 34))
        foods.add(FoodItem("Chicken Burger", "1 slice", 210, 23, 34))
        foods.add(FoodItem("Veggie Burger", "1 slice", 210, 23, 34))
        foods.add(FoodItem("Aloo Tikki Burger", "1 slice", 210, 23, 34))
        foods.add(FoodItem("Tuna Salad", "1 bowl", 176, 45, 23))
        foods.add(FoodItem("Veggie Salad", "1 bowl", 176, 45, 23))
        foods.add(FoodItem("Caesar Salad", "1 bowl", 176, 45, 23))
        foods.add(FoodItem("Honey Oats", "20 Grams", 83, 34, 56))
        foods.add(FoodItem("Mango Smoothie", "20 Grams", 83, 34, 56))
        foods.add(FoodItem("Pancakes", "20 Grams", 83, 34, 56))
        foods.add(FoodItem("Chicken Tacos", "20 Grams", 83, 34, 56))
        foods.add(FoodItem("Veggie Tacos", "20 Grams", 83, 34, 56))

        binding.foodItems.layoutManager = LinearLayoutManager(this)
        mFoodAdapter = FoodAdapter(foods)
        binding.foodItems.adapter = mFoodAdapter
        setUpSearchView()
    }

    private fun setUpSearchView() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mFoodAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mFoodAdapter.getFilter().filter(newText);
                return true
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home->{
                finish()
                return true
            }
        }
        super.onOptionsItemSelected(item)
        return false
    }

    companion object {
        @JvmStatic val TAG = "MealLoggerActivity"
    }
}