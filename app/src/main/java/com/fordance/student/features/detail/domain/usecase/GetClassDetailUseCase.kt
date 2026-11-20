package com.fordance.student.features.detail.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.ClassDetail
import com.fordance.student.features.detail.domain.repository.DetailRepository
import javax.inject.Inject

class GetClassDetailUseCase @Inject constructor(
    private val repository: DetailRepository
) {
    suspend operator fun invoke(classId: String): ResultWrapper<ClassDetail> {
        return repository.getClassDetail(classId)
    }
}
