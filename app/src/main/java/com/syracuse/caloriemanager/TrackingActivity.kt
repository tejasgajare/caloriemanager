package com.syracuse.caloriemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.syracuse.caloriemanager.databinding.ActivityTrackingBinding
import com.syracuse.caloriemanager.models.User

class TrackingActivity : AppCompatActivity()  {
    lateinit var binding: ActivityTrackingBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var fUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityTrackingBinding.inflate(layoutInflater)



        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().reference
        fUser = firebaseAuth.currentUser!!

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.title=""

        onboardUser()
        Log.wtf("firebase", "Tracking activity started for ${fUser.uid}")
        binding.bottomNavigation.setOnItemSelectedListener{item ->
            when (item.itemId) {
                R.id.page_dashboard -> {
                    val fragment = DashboardFragment()
                    binding.title.text = getString(R.string.title_dashboard)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DASHBOARD)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.page_diary -> {
                    val fragment = DiaryFragment()
                    binding.title.text = getString(R.string.title_diary)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DIARY)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.page_newsfeed -> {
                    val fragment = NewsFeedFragment()
                    binding.title.text = getString(R.string.title_newsfeed)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DIARY)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.page_settings -> {
                    val fragment = SettingsFragment()
                    binding.title.text = getString(R.string.title_settings)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DIARY)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun onboardUser() {
        Log.wtf("firebase", "Reading data for user ${fUser.uid}")
        var mUser = User()
        firebaseDatabase.child("users").child(fUser.uid).get()
            .addOnSuccessListener { snapshot->
                try {
                    mUser = snapshot.getValue(User::class.java)!!
                } catch (error: NullPointerException) {
                    Log.wtf(DashboardFragment.TAG,"User has not completed On-boarding, redirecting to settings page ")
                    Snackbar.make(binding.root, "Please Go to Settings page to complete On Boarding", Snackbar.LENGTH_SHORT).show()
                }
                Log.wtf(TAG, "Got value ${snapshot.value}")
            }.addOnFailureListener{
                Log.wtf(TAG,"Could Not Found the user in database")
            }.addOnCompleteListener {
                Log.wtf(TAG,"Completed: Could Not Found the user in database")
                if (!mUser.isOnBoarded){

                }
            }.addOnCanceledListener {
                Log.wtf(TAG,"Cancelled: Could Not Found the user in database")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> {
                firebaseAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        super.onOptionsItemSelected(item)
        return true
    }

    companion object {
        @JvmStatic val TAG = "TrackingActivity"
        @JvmStatic val FRAGMENT_DASHBOARD = "FRAGMENT_DASHBOARD"
        @JvmStatic val FRAGMENT_DIARY = "FRAGMENT_DIARY"
        @JvmStatic val FRAGMENT_NEWSFEED = "FRAGMENT_NEWSFEED"
        @JvmStatic val FRAGMENT_SETTINGS = "FRAGMENT_SETTINGS"
    }
}