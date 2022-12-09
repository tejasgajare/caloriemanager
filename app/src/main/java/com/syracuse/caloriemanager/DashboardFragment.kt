package com.syracuse.caloriemanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.syracuse.caloriemanager.databinding.FragmentDashboardBinding
import com.syracuse.caloriemanager.models.ActivityLevel
import com.syracuse.caloriemanager.models.User
import java.text.SimpleDateFormat
import java.util.*


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var fUser: FirebaseUser
    private lateinit var todayDate: String

    private var goalCalories: Double = 0.0
    private var goalFats: Double = 0.0
    private var goalCarbohydrates: Double = 0.0
    private var goalProteins: Double = 0.0

    private var consumedCalories: Double = 0.0
    private var consumedFats: Double = 0.0
    private var consumedCarbohydrates: Double = 0.0
    private var consumedProteins: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        fUser = firebaseAuth.currentUser!!
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        todayDate = sdf.format(Date())
        Log.v(TAG, "Dashboard Fragment Loaded")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGoalValues()
        setConsumedValues()
    }


    private fun setGoalValues() {
        firebaseDatabase.reference
            .child("users")
            .child(fUser.uid)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var mUser = User()

                    try {
                        mUser = snapshot.getValue(User::class.java)!!
                    } catch (error: NullPointerException) {
                        Log.wtf(TAG,"User has not completed On-boarding, redirecting to settings page ")
                    }

                    val bmr = 655 + (4.35 * mUser.currentWeight) + (4.7 * mUser.height) - (4.7 * mUser.age)
                    var maintenanceCalories = bmr * enumValueOf<ActivityLevel>(mUser.activityLevel).value

                    if(mUser.goalWeight < mUser.currentWeight){
                        // Weight Loss
                        goalCalories = maintenanceCalories - 100
                    } else if (mUser.goalWeight > mUser.currentWeight) {
                        // Weight Gain
                        goalCalories = maintenanceCalories + 100
                    }

                    goalFats = (goalCalories * 0.30) / 9
                    goalCarbohydrates = (goalCalories * 0.31) / 4
                    goalProteins = (goalCalories * 0.29) / 4

                    binding.goalCalories.text = goalCalories.toInt().toString()

                    // Set the progress bar's max values
                    binding.consumedCalories.max = goalCalories.toInt()
                    binding.consumedFats.max = goalFats.toInt()
                    binding.consumedCarbohydrates.max = goalCarbohydrates.toInt()
                    binding.consumedProteins.max = goalProteins.toInt()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun setConsumedValues(){
        Log.wtf(TAG, "Loading dashboard data for date $todayDate")

        val mealsQuery = firebaseDatabase
            .reference
            .child("meals")
            .child(fUser.uid)
            .child(todayDate)

        mealsQuery
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tCalories = 0.0
                    var tFats = 0.0
                    var tCarbohydrates = 0.0
                    var tProteins = 0.0
                    val breakfast = dataSnapshot.child("breakfast")
                    val lunch = dataSnapshot.child("lunch")
                    val dinner = dataSnapshot.child("dinner")

                    for (data in breakfast.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        val fats: Long = data.child("fats").getValue(Long::class.java)!!
                        val carbohydrates: Long = data.child("carbohydrates").getValue(Long::class.java)!!
                        val proteins: Long = data.child("proteins").getValue(Long::class.java)!!
                        tCalories += calories
                        tFats += fats
                        tCarbohydrates += carbohydrates
                        tProteins += proteins
                    }

                    for (data in lunch.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        val fats: Long = data.child("fats").getValue(Long::class.java)!!
                        val carbohydrates: Long = data.child("carbohydrates").getValue(Long::class.java)!!
                        val proteins: Long = data.child("proteins").getValue(Long::class.java)!!
                        tCalories += calories
                        tFats += fats
                        tCarbohydrates += carbohydrates
                        tProteins += proteins
                    }

                    for (data in dinner.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        val fats: Long = data.child("fats").getValue(Long::class.java)!!
                        val carbohydrates: Long = data.child("carbohydrates").getValue(Long::class.java)!!
                        val proteins: Long = data.child("proteins").getValue(Long::class.java)!!
                        tCalories += calories
                        tFats += fats
                        tCarbohydrates += carbohydrates
                        tProteins += proteins
                    }
                    // Setting Total Consumed Values for the Day
                    consumedCalories = tCalories
                    consumedFats = tFats
                    consumedCarbohydrates = tCarbohydrates
                    consumedProteins = tProteins

                    // Set the text values on Dashboard UI
                    binding.todayCalories.text = consumedCalories.toInt().toString()
                    binding.todayFats.text = consumedFats.toInt().toString() + "⧸" + goalFats.toInt().toString() + " Left"
                    binding.todayCarbohydrates.text = consumedCarbohydrates.toInt().toString() + "⧸" + goalCarbohydrates.toInt().toString() + " Left"
                    binding.todayProtein.text = consumedProteins.toInt().toString() + "⧸" + goalProteins.toInt().toString() + " Left"

                    // Set the progress bar's progress
                    binding.consumedCalories.progress = consumedCalories.toInt()
                    binding.consumedFats.progress = consumedFats.toInt()
                    binding.consumedCarbohydrates.progress = consumedCarbohydrates.toInt()
                    binding.consumedProteins.progress = consumedProteins.toInt()

                    // Set progress bar inner text
                    binding.remainingCalories.text = (goalCalories.toInt() - consumedCalories.toInt()).toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    companion object {
        @JvmStatic val TAG = "DashboardFragment"
    }
}