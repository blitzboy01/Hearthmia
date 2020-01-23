package com.soumio.inceptiontutorial.ui.Help.warning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpWarning : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_warning, container, false)


        val buttonWarning: Button = root.findViewById(R.id.help_warning_button)

        buttonWarning.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_warning_to_nav_help_disclaimer)
        }

        return root
    }
}