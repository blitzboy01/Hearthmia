package com.soumio.inceptiontutorial.ui.Help.help_disclaimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpDisclaimerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_disclaimer, container, false)

/*

        val buttonDisclaimer: Button = root.findViewById(R.id.help_disclaimer_button)

        buttonDisclaimer.setOnClickListener {
            it.findNavController().navigate(R.id)
        }
*/

        return root
    }
}