package com.fordance.student.features.detail.presentation.courseclass

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.detail.domain.usecase.GetClassDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val getClassDetailUseCase: GetClassDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val classId: String = checkNotNull(savedStateHandle["classId"])

    private val _uiState = MutableStateFlow<ClassDetailUiState>(ClassDetailUiState.Loading)
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()

    init {
        loadClassDetail()
    }

    fun loadClassDetail() {
        viewModelScope.launch {
            _uiState.value = ClassDetailUiState.Loading
            when (val result = getClassDetailUseCase(classId)) {
                is ResultWrapper.Success -> {
                    _uiState.value = ClassDetailUiState.Success(danceClass = result.data)
                }
                is ResultWrapper.Error -> {
                    _uiState.value = ClassDetailUiState.Error(result.message)
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    fun enrollClass() {
        _uiState.update { state ->
            (state as? ClassDetailUiState.Success)?.copy(
                isEnrolled = true,
                message = "Đã đăng ký khóa học thành công!"
            ) ?: state
        }
    }

    fun clearMessage() {
        _uiState.update { state ->
            (state as? ClassDetailUiState.Success)?.copy(message = null) ?: state
        }
    }
}
