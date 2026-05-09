// bite_app/app/src/main/java/com/foodRescue/viewmodel/VolunteerViewModel.kt
package com.foodRescue.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodRescue.data.model.VolunteerImpact
import com.foodRescue.data.model.VolunteerMission
import com.foodRescue.data.repository.VolunteerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "VolunteerViewModel"

class VolunteerViewModel : ViewModel() {

    private val repo = VolunteerRepository()
    private val auth = FirebaseAuth.getInstance()
    private val uid  get() = auth.currentUser?.uid ?: ""

    // ── Online / offline toggle ───────────────────────────────────────────────
    private val _isOnline = MutableStateFlow(false)
    val isOnline = _isOnline.asStateFlow()

    fun setOnline(online: Boolean) {
        if (uid.isBlank()) {
            Log.w(TAG, "setOnline: no authenticated user")
            return
        }
        viewModelScope.launch {
            try {
                Log.d(TAG, "setOnline: $online")
                repo.setAvailability(uid, online)
                _isOnline.value = online
                Log.d(TAG, "setOnline: ✅ availability updated")
            } catch (e: Exception) {
                Log.e(TAG, "setOnline: ❌ ${e.message}", e)
                _error.value = "Could not update availability: ${e.message}"
            }
        }
    }

    // ── Active mission ────────────────────────────────────────────────────────
    val activeMission: StateFlow<VolunteerMission?> = flow {
        if (uid.isNotBlank()) {
            repo.listenActiveMission(uid).collect { emit(it) }
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // ── Mission actions ───────────────────────────────────────────────────────
    private val _actionInProgress = MutableStateFlow(false)
    val actionInProgress = _actionInProgress.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun clearError() { _error.value = null }

    fun markPickedUp() {
        val mission = activeMission.value ?: return
        viewModelScope.launch {
            _actionInProgress.value = true
            try {
                Log.d(TAG, "markPickedUp: missionId=${mission.id}")
                repo.markPickedUp(mission.id, mission.donationId)
                Log.d(TAG, "markPickedUp: ✅")
            } catch (e: Exception) {
                Log.e(TAG, "markPickedUp: ❌ ${e.message}", e)
                _error.value = "Failed to confirm pickup: ${e.message}"
            } finally {
                _actionInProgress.value = false
            }
        }
    }

    fun markDelivered() {
        val mission = activeMission.value ?: return
        viewModelScope.launch {
            _actionInProgress.value = true
            try {
                Log.d(TAG, "markDelivered: missionId=${mission.id}")
                repo.markDelivered(mission.id, mission.donationId)
                Log.d(TAG, "markDelivered: ✅")
            } catch (e: Exception) {
                Log.e(TAG, "markDelivered: ❌ ${e.message}", e)
                _error.value = "Failed to confirm delivery: ${e.message}"
            } finally {
                _actionInProgress.value = false
            }
        }
    }

    // ── Impact stats ──────────────────────────────────────────────────────────
    private val _impact = MutableStateFlow(VolunteerImpact())
    val impact = _impact.asStateFlow()

    private val _history = MutableStateFlow<List<VolunteerMission>>(emptyList())
    val history = _history.asStateFlow()

    init {
        loadImpact()
    }

    fun loadImpact() {
        if (uid.isBlank()) return
        viewModelScope.launch {
            try {
                Log.d(TAG, "loadImpact: fetching for uid=$uid")
                _impact.value  = repo.getImpactStats(uid)
                _history.value = repo.getDeliveryHistory(uid)
                Log.d(TAG, "loadImpact: ✅ ${_impact.value}")
            } catch (e: Exception) {
                Log.e(TAG, "loadImpact: ❌ ${e.message}", e)
            }
        }
    }
}
