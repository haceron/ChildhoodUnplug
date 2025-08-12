package com.ppp.pegasussociety.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Repository.Repository
import com.ppp.pegasussociety.Screens.ActivityBannerItem
import com.ppp.pegasussociety.Screens.ActivityCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class BannerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _bannerItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val bannerItemsState: StateFlow<List<ActivityBannerItem>> = _bannerItemsState.asStateFlow()

    private val _latestItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val latestItemsState: StateFlow<List<ActivityBannerItem>> = _latestItemsState.asStateFlow()

    private val _popularItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val popularItemsState: StateFlow<List<ActivityBannerItem>> = _popularItemsState.asStateFlow()

    private val _selectedArticleState = MutableStateFlow<ActivityBannerItem?>(null)
    val selectedArticleState: StateFlow<ActivityBannerItem?> = _selectedArticleState.asStateFlow()

    init {
        fetchBanners()
        fetchLatest()
        fetchPopular()
    }

    private fun fetchBanners() {
        viewModelScope.launch {
            try {
                val posts = repository.getActivityBanners()
                _bannerItemsState.value = posts.map {
                    ActivityBannerItem(
                        id = it.id,
                        title = it.title,
                        imageUrl = it.imageUrl,
                        bgColor = Color(0xFFE0F7FA),
                        content = it.content,
                        attachmentUrl = it.attachmentUrl
                    )
                }
            } catch (e: Exception) {
                _bannerItemsState.value = emptyList()
            }
        }
    }

    private fun fetchLatest() {
        viewModelScope.launch {
            try {
                val posts = repository.getActivityLatest()
                _latestItemsState.value = posts.map {
                    ActivityBannerItem(
                        id = it.id,
                        title = it.title,
                        imageUrl = it.imageUrl,
                        bgColor = Color(0xFFE0F7FA),
                        content = it.content,
                        attachmentUrl = it.attachmentUrl

                    )
                }
            } catch (e: Exception) {
                _latestItemsState.value = emptyList()
            }
        }
    }

    private fun fetchPopular() {
        viewModelScope.launch {
            try {
                val posts = repository.getActivityPopular()
                _popularItemsState.value = posts.map {
                    ActivityBannerItem(
                        id = it.id,
                        title = it.title,
                        imageUrl = it.imageUrl,
                        bgColor = Color(0xFFE0F7FA),
                        content = it.content,
                        attachmentUrl = it.attachmentUrl

                    )
                }
            } catch (e: Exception) {
                _popularItemsState.value = emptyList()
            }
        }
    }

    private val _selectedActivity = MutableStateFlow<ActivityBannerItem?>(null)
    val selectedActivity: StateFlow<ActivityBannerItem?> = _selectedActivity.asStateFlow()

    fun fetchActivityById(id: Int) {
        viewModelScope.launch {
            try {
                // Try finding it from the already loaded lists first
                val existing = _bannerItemsState.value
                    .plus(_latestItemsState.value)
                    .plus(_popularItemsState.value)
                    .find { it.id == id }

                if (existing != null) {
                    _selectedActivity.value = existing
                } else {
                    // If not found locally, load from repository
                    val result = repository.getActivityById(id)
                    if (result != null) {
                        _selectedActivity.value = ActivityBannerItem(
                            id = result.id,
                            title = result.title,
                            imageUrl = result.imageUrl,
                            bgColor = Color(0xFFE0F7FA),
                            content = result.content,
                            attachmentUrl = result.attachmentUrl

                        )
                    }
                }
            } catch (e: Exception) {
                _selectedActivity.value = null
            }
        }
    }

    private val _categoryActivitiesState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val categoryActivitiesState = _categoryActivitiesState.asStateFlow()

    fun fetchActivitiesByCategory(category: String) = viewModelScope.launch {
        try {
            val posts = repository.getActivitiesByCategory(category)
            _categoryActivitiesState.value = posts.map {
                ActivityBannerItem(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    bgColor = Color(0xFFE0F7FA),
                    content = it.content,
                    attachmentUrl = it.attachmentUrl

                )
            }
        } catch (e: Exception) {
            _categoryActivitiesState.value = emptyList()
        }    }

}


