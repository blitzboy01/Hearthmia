package com.soumio.inceptiontutorial.ui.ECGBasic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class ECGBasicsFragment4 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_ecg_basics_4, container, false)
        val btnecgbasics4: Button = root.findViewById(R.id.basics_4_next_button)

        btnecgbasics4.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_ecgbasics4_to_nav_ecgbasics5)
        }

        return root
    }
}