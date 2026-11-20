package com.fordance.student.features.detail.di

import com.fordance.student.features.detail.data.repository.DetailRepositoryImpl
import com.fordance.student.features.detail.domain.repository.DetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DetailModule {

    @Binds
    @Singleton
    abstract fun bindDetailRepository(
        impl: DetailRepositoryImpl
    ): DetailRepository
}
