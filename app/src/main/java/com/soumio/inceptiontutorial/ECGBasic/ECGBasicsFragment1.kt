package com.soumio.inceptiontutorial.ECGBasic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class ECGBasicsFragment1 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_ecg_basics_1, container, false)
        val btnecgbasics1: Button = root.findViewById(R.id.basics_1_next_button)

        btnecgbasics1.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_ecgbasics_to_nav_ecgbasics2)
        }

        return root
    }
}