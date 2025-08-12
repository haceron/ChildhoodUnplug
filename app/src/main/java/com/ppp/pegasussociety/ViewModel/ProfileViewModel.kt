package com.ppp.pegasussociety.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ppp.pegasussociety.Model.ChildProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profiles = mutableStateListOf<ChildProfile>()
    val profiles: List<ChildProfile> = _profiles

    fun addProfile(profile: ChildProfile) {
        _profiles.add(profile)
    }
}
