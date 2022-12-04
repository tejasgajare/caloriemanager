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
import com.syracuse.caloriemanager.databinding.FragmentDairyBinding
import com.syracuse.caloriemanager.models.MealItem
import com.syracuse.caloriemanager.views.MealAdapter
import java.text.SimpleDateFormat
import java.util.*

class DairyFragment : Fragment(), View.OnClickListener, MealAdapter.ItemClickListener {
    private var _binding: FragmentDairyBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMealBreakfastAdapter: MealAdapter
    private lateinit var mMealLunchAdapter: MealAdapter
    private lateinit var mMealDinnerAdapter: MealAdapter
    private lateinit var firebaseAuth: FirebaseAuth

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.breakfastMeals.layoutManager = WrapContentLinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.lunchMeals.layoutManager = WrapContentLinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.dinnerMeals.layoutManager = WrapContentLinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

        binding.addItemBreakfast.setOnClickListener(this)
        binding.addItemLunch.setOnClickListener(this)
        binding.addItemDinner.setOnClickListener(this)

        binding.removeItemBreakfast.setOnClickListener(this)
        binding.removeItemLunch.setOnClickListener(this)
        binding.removeItemDinner.setOnClickListener(this)

        val user = firebaseAuth.currentUser
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        val breakfastQuery = FirebaseDatabase.getInstance()
            .reference
            .child("meals")
            .child(user?.uid.toString())
            .child(currentDate)
            .child("breakfast")
            .limitToFirst(10)

        val lunchQuery = FirebaseDatabase.getInstance()
            .reference
            .child("meals")
            .child(user?.uid.toString())
            .child(currentDate)
            .child("lunch")
            .limitToFirst(10)

        val dinnerQuery = FirebaseDatabase.getInstance()
            .reference
            .child("meals")
            .child(user?.uid.toString())
            .child(currentDate)
            .child("dinner")
            .limitToFirst(10)

        mMealBreakfastAdapter = MealAdapter(MealItem::class.java, breakfastQuery)
        mMealBreakfastAdapter.setItemClickListener(this)

        mMealLunchAdapter = MealAdapter(MealItem::class.java, lunchQuery)
        mMealLunchAdapter.setItemClickListener(this)

        mMealDinnerAdapter = MealAdapter(MealItem::class.java, dinnerQuery)
        mMealDinnerAdapter.setItemClickListener(this)

        binding.breakfastMeals.adapter = mMealBreakfastAdapter
        binding.lunchMeals.adapter = mMealLunchAdapter
        binding.dinnerMeals.adapter = mMealDinnerAdapter
    }


    override fun onClick(view: View) {
        when(view.id){
            R.id.add_item_breakfast->{
                val intent = Intent(activity, FoodActivity::class.java)
                intent.putExtra("type",resources.getString(R.string.meal_breakfast));
                this.startActivity(intent)
            }
            R.id.remove_item_breakfast->{
                if(mMealBreakfastAdapter.getRemoveState()){
                    binding.removeItemBreakfast.text = "Remove"
                    mMealBreakfastAdapter.setRemoveState(false)
                } else {
                    binding.removeItemBreakfast.text = "Cancel"
                    mMealBreakfastAdapter.setRemoveState(true)
                }
            }

            R.id.add_item_lunch->{
                val intent = Intent(activity, FoodActivity::class.java)
                intent.putExtra("type",resources.getString(R.string.meal_lunch));
                this.startActivity(intent)
            }
            R.id.remove_item_lunch-> {
                if(mMealLunchAdapter.getRemoveState()){
                    binding.removeItemLunch.text = "Remove"
                    mMealLunchAdapter.setRemoveState(false)
                } else {
                    binding.removeItemLunch.text = "Cancel"
                    mMealLunchAdapter.setRemoveState(true)
                }
            }

            R.id.add_item_dinner->{
                val intent = Intent(activity, FoodActivity::class.java)
                intent.putExtra("type",resources.getString(R.string.meal_dinner));
                this.startActivity(intent)
            }
            R.id.remove_item_dinner-> {
                if(mMealDinnerAdapter.getRemoveState()){
                    binding.removeItemDinner.text = "Remove"
                    mMealDinnerAdapter.setRemoveState(false)
                } else {
                    binding.removeItemDinner.text = "Cancel"
                    mMealDinnerAdapter.setRemoveState(true)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMealBreakfastAdapter.startListening()
        mMealLunchAdapter.startListening()
        mMealDinnerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mMealBreakfastAdapter.stopListening()
        mMealLunchAdapter.stopListening()
        mMealDinnerAdapter.stopListening()
    }

    override fun onMealItemRemove(view: View) {
        Log.e(MealAdapter.TAG, "Item Removed")
        view.visibility = View.GONE
        binding.removeItemBreakfast.text = "Remove"
        binding.removeItemLunch.text = "Remove"
        binding.removeItemDinner.text = "Remove"

        mMealBreakfastAdapter.setRemoveState(false)
        mMealLunchAdapter.setRemoveState(false)
        mMealDinnerAdapter.setRemoveState(false)
    }

    companion object {
        @JvmStatic val TAG = "DairyFragment"
    }


}