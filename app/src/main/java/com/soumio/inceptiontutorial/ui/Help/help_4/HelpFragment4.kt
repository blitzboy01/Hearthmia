package com.soumio.inceptiontutorial.ui.Help.help_4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpFragment4 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_help_4, container, false)


        val buttonNext4: Button = root.findViewById(R.id.help_button_4)

        buttonNext4.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_help_4_to_nav_help_5)
        }

        return root
    }
}