package com.ppp.pegasussociety.ViewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppp.pegasussociety.Repository.Repository
import com.ppp.pegasussociety.Screens.ActivityBannerItem
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

    // --- States for Banners, Latest, and Popular ---
    private val _bannerItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val bannerItemsState: StateFlow<List<ActivityBannerItem>> = _bannerItemsState.asStateFlow()

    private val _latestItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val latestItemsState: StateFlow<List<ActivityBannerItem>> = _latestItemsState.asStateFlow()

    private val _popularItemsState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val popularItemsState: StateFlow<List<ActivityBannerItem>> = _popularItemsState.asStateFlow()


    // --- State for a single selected activity/article ---
    private val _selectedActivity = MutableStateFlow<ActivityBannerItem?>(null)
    val selectedActivity: StateFlow<ActivityBannerItem?> = _selectedActivity.asStateFlow()


    // --- States for Category-specific activities ---
    private val _categoryActivitiesState = MutableStateFlow<List<ActivityBannerItem>>(emptyList())
    val categoryActivitiesState = _categoryActivitiesState.asStateFlow()

    // ✨ 1. isLoading state is added here
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    init {
        fetchBanners()
        fetchLatest()
        fetchPopular()
    }

    // --- Data fetching functions ---

    private fun fetchBanners() {
        viewModelScope.launch {
            try {
                val posts = repository.getActivityBanners()
                _bannerItemsState.value = posts.map {
                    // ... mapping logic
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
                    // ... mapping logic
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
                    // ... mapping logic
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

    fun fetchActivityById(id: Int) {
        viewModelScope.launch {
            try {
                // ... (rest of the function is unchanged)
                val existing = _bannerItemsState.value
                    .plus(_latestItemsState.value)
                    .plus(_popularItemsState.value)
                    .find { it.id == id }

                if (existing != null) {
                    _selectedActivity.value = existing
                } else {
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

    // ✨ 2. This function is now updated to manage the isLoading state
    fun fetchActivitiesByCategory(category: String) = viewModelScope.launch {
        _isLoading.value = true // Set loading to true before starting
        _categoryActivitiesState.value = emptyList() // Clear old data to prevent showing stale content
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
            _categoryActivitiesState.value = emptyList() // Handle error case
        } finally {
            _isLoading.value = false // Set loading to false when done (success or fail)
        }
    }
}


