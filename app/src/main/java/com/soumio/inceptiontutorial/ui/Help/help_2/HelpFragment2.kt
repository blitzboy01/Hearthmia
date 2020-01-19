package com.soumio.inceptiontutorial.ui.Help.help_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpFragment2 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_help_2, container, false)

        val buttonNext2: Button = root.findViewById(R.id.help_button_2)

        buttonNext2.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_help_2_to_nav_help_3)
        }



        return root
    }
}