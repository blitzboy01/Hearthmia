package com.soumio.inceptiontutorial.ui.Help.help_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpFragment5 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_help_5, container, false)


        val buttonNext5: Button = root.findViewById(R.id.help_button_5)

        buttonNext5.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_help_5_to_nav_warning)
        }

        return root
    }
}