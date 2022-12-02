package com.syracuse.caloriemanager

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syracuse.caloriemanager.databinding.FragmentDashboardBinding
import org.eazegraph.lib.models.PieModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "Dashboard Fragment Loaded")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val mPieChart = binding.piechart

        mPieChart.addPieSlice(PieModel("Remaining", 800F, Color.parseColor("#D9D9D9")))
        mPieChart.addPieSlice(PieModel("Completed", 1200F, Color.parseColor("#00701A")))
        mPieChart.startAnimation()
        return binding.root
    }

    companion object {
        @JvmStatic val TAG = "DashboardFragment"
    }
}