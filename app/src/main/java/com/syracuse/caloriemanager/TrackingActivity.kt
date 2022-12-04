package com.syracuse.caloriemanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.syracuse.caloriemanager.databinding.ActivityTrackingBinding

class TrackingActivity : AppCompatActivity()  {
    lateinit var binding: ActivityTrackingBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.title=""
        val navigation = binding.bottomNavigation
        navigation.setOnItemSelectedListener{item ->
            when (item.itemId) {
                R.id.page_dashboard -> {
                    val fragment = DashboardFragment()
                    binding.title.text = getString(R.string.title_dashboard)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DASHBOARD)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.page_dairy -> {
                    val fragment = DairyFragment()
                    binding.title.text = getString(R.string.title_dairy)
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.fragmentHost.id, fragment, FRAGMENT_DAIRY)
                    transaction.commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
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
        @JvmStatic val FRAGMENT_DAIRY = "FRAGMENT_DAIRY"
    }
}