package com.syracuse.caloriemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.syracuse.caloriemanager.databinding.FragmentDairyBinding
import com.syracuse.caloriemanager.models.FoodItem
import com.syracuse.caloriemanager.models.MealItem
import com.syracuse.caloriemanager.views.MealAdapter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class DairyFragment : Fragment(), View.OnClickListener, MealAdapter.ItemClickListener {
    private var _binding: FragmentDairyBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMealAdapter: MealAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    val query = FirebaseDatabase.getInstance()
        .reference
        .child("meals")
        .child("zO69BhHjHPXfKvjpzLogYTNNnHE3")
        .child("2022-12-03")
        .child("breakfast")
        .limitToFirst(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        Log.v(TAG, "Dairy Fragment Loaded")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDairyBinding.inflate(inflater, container, false)
        binding.breakfastMeals.layoutManager = LinearLayoutManager(activity)
        binding.addItemBreakfast.setOnClickListener(this)
        binding.removeItemBreakfast.setOnClickListener(this)

        val breakfast = ArrayList<MealItem>()
        val user = firebaseAuth.currentUser
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())



//        breakfast.add(MealItem("Bread", "5 Slices", 5, 213, 43, 41))
//        breakfast.add(MealItem("Peanut Butter", "30 Grams", 1, 213, 34, 32))
//
//        for(meal in breakfast){
//            Log.e(TAG, "Meal : ${meal.name}  ${meal.unit}  ${meal.calories}")
//        }

        mMealAdapter = MealAdapter(MealItem::class.java, query)
        mMealAdapter.setItemClickListener(this)
        binding.breakfastMeals.adapter = mMealAdapter
        return binding.root
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.add_item_breakfast->{
                val intent = Intent(activity, FoodActivity::class.java)
                intent.putExtra("type",resources.getString(R.string.meal_breakfast));
                this.startActivity(intent)
            }
            R.id.remove_item_breakfast->{
                if(mMealAdapter.getRemoveState()){
                    binding.removeItemBreakfast.text = "Remove"
                    mMealAdapter.setRemoveState(false)
                } else {
                    binding.removeItemBreakfast.text = "Cancel"
                    mMealAdapter.setRemoveState(true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMealAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mMealAdapter.stopListening()
    }

    override fun onMealItemRemove(view: View) {
        binding.removeItemBreakfast.text = "Remove"
        view.visibility = View.GONE
        mMealAdapter.setRemoveState(false)
    }

    companion object {
        @JvmStatic val TAG = "DairyFragment"
    }


}