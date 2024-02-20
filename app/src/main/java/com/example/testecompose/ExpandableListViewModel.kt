package com.example.testecompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpandableListViewModel : ViewModel() {

    private val itemsList = MutableStateFlow(listOf<DataModel>())
    val items: StateFlow<List<DataModel>> get() = itemsList

    private val itemsIdList = MutableStateFlow(listOf<Int>())
    val itemsIds: StateFlow<List<Int>> get() = itemsIdList

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                itemsList.emit(Data.items)
            }
        }
    }

    fun onItemClicked(itemId: Int) {
        itemsIdList.value = itemsIdList.value.toMutableList().also { list ->
            if (list.contains(itemId)) {
                list.remove(itemId)
            } else {
                list.add(itemId)
            }
        }
    }

}