package com.northern.websocketapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<Pair<Boolean, String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages

    fun addMessage(message: Pair<Boolean, String>) = uiScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = uiScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }
}