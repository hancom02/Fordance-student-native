package com.fordance.student.features.catalog.di

import com.fordance.student.features.catalog.data.repository.CatalogRepositoryImpl
import com.fordance.student.features.catalog.domain.repository.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogModule {

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        impl: CatalogRepositoryImpl
    ): CatalogRepository
}
