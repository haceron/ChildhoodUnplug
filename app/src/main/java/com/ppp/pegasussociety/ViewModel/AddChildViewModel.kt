package com.ppp.pegasussociety.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Model.AddChildRequest
import com.ppp.pegasussociety.Model.Child
import com.ppp.pegasussociety.Model.ChildrenResponse
import com.ppp.pegasussociety.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChildViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // List of actual child objects, not the wrapper
    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> get() = _children

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> get() = _status

    fun loadChildren(parentId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getChild(parentId) // returns ChildrenResponse
                _children.value = response.children // actual list from API
            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
            }
        }
    }

    fun addChild(
        childrenName: String,
        parentId: String,
        interest: List<String>,
        focusArea: List<String>,
        gender: String,
        DOB: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.addChild(
                    childrenName = childrenName,
                    parentId = parentId,
                    interest = interest,
                    focusArea = focusArea,
                    gender = gender,
                    DOB = DOB
                )
                _status.value = response.message
                loadChildren(parentId) // refresh list after adding
            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
            }
        }
    }
}
