package com.soumio.inceptiontutorial.ui.Help.help_instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.soumio.inceptiontutorial.R

class HelpInstructionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_instruction, container, false)


        val buttonInstruction: Button = root.findViewById(R.id.help_instruction_button)

        buttonInstruction.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_help_instruction_to_nav_help)
        }

        return root
    }
}