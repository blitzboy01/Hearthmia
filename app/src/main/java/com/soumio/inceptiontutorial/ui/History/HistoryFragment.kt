package com.soumio.inceptiontutorial.ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.soumio.inceptiontutorial.Adapter.HistoryAdapter
import com.soumio.inceptiontutorial.R
import com.soumio.inceptiontutorial.model.History
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        val recyclerViewHistory = root.recyclerViewHistory

        recyclerViewHistory.layoutManager = GridLayoutManager(context, 1)
        recyclerViewHistory.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        historyViewModel.getListHistory().observe(this, Observer {
            val items: List<History> = it
            recyclerViewHistory.adapter = HistoryAdapter(items, root.context)
        })

        return root

    }
}