package com.syracuse.caloriemanager

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.syracuse.caloriemanager.databinding.FragmentDairyBinding
import com.syracuse.caloriemanager.databinding.FragmentDashboardBinding
import com.syracuse.caloriemanager.models.MealItem
import com.syracuse.caloriemanager.views.MealAdapter
import org.eazegraph.lib.models.PieModel

class DairyFragment : Fragment() {
    private var _binding: FragmentDairyBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "Dairy Fragment Loaded")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDairyBinding.inflate(inflater, container, false)
        binding.breakfastMeals.layoutManager = LinearLayoutManager(activity)

        val breakfast = ArrayList<MealItem>()
        breakfast.add(MealItem("Bread", "5 Slices", 210))
        breakfast.add(MealItem("Peanut Butter", "30 Grams", 176))
        breakfast.add(MealItem("Jam", "20 Grams", 83))

        mMealAdapter = MealAdapter(breakfast)
        binding.breakfastMeals.adapter = mMealAdapter
        return binding.root
    }

    companion object {
        @JvmStatic val TAG = "DairyFragment"
    }
}