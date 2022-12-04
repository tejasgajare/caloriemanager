package com.syracuse.caloriemanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.syracuse.caloriemanager.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
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
        binding.progress.progress = 90
        binding.progress.max = 100

        val user = firebaseAuth.currentUser
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        val mealsQuery = FirebaseDatabase.getInstance()
            .reference
            .child("meals")
            .child(user?.uid.toString())
            .child(currentDate)

        mealsQuery
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var tCalories = 0L
                    val breakfast = dataSnapshot.child("breakfast")
                    val lunch = dataSnapshot.child("lunch")
                    val dinner = dataSnapshot.child("dinner")

                    for (data in breakfast.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        tCalories += calories
                    }

                    for (data in lunch.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        tCalories += calories
                    }

                    for (data in dinner.children) {
                        val calories: Long = data.child("calories").getValue(Long::class.java)!!
                        tCalories += calories
                    }
                    binding.todayCalories.text = tCalories.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    companion object {
        @JvmStatic val TAG = "DashboardFragment"
    }
}