package com.syracuse.caloriemanager

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syracuse.caloriemanager.databinding.FragmentDairyBinding
import com.syracuse.caloriemanager.databinding.FragmentDashboardBinding
import org.eazegraph.lib.models.PieModel

class DairyFragment : Fragment() {
    private var _binding: FragmentDairyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "Dairy Fragment Loaded")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDairyBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic val TAG = "DairyFragment"
    }
}