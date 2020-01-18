package com.soumio.inceptiontutorial.ui.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.ui.Analyze.ChooseModel
import com.soumio.inceptiontutorial.R

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val btnEcgBasics: ImageButton = root.findViewById(R.id.ecg_basics_button)
        btnEcgBasics.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_ecgbasics)
        }

        val btnAnalyze: ImageButton = root.findViewById(R.id.analyze_button)
        btnAnalyze.setOnClickListener {
            Intent(context, ChooseModel::class.java).also { intent ->
                startActivity(intent)
            }
//            it.findNavController().navigate(R.id.action_nav_home_to_nav_analyze)
        }

        val btnHistory: ImageButton = root.findViewById(R.id.history_button)
        btnHistory.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_history)
        }


        val btnHelp: ImageButton = root.findViewById(R.id.help_button)
        btnHelp.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_help)

        }


    return root

}
}
