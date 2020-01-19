package com.soumio.inceptiontutorial.ui.ECGBasic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class ECGBasicsFragment3 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_ecg_basics_3, container, false)
        val btnecgbasics3: Button = root.findViewById(R.id.basics_3_next_button)

        btnecgbasics3.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_ecgbasics3_to_nav_ecgbasics4)
        }

        return root
    }
}