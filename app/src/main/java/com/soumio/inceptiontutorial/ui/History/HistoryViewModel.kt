package com.soumio.inceptiontutorial.ui.History

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soumio.inceptiontutorial.model.History

class HistoryViewModel : ViewModel() {

    private val listHistory = MutableLiveData<List<History>>()

    fun getListHistory(): LiveData<List<History>> {
        listHistory.value = listOf(
                History(),
                History(),
                History(),
                History(),
                History(),
                History(),
                History(),
                History()
        )
        return listHistory
    }
}