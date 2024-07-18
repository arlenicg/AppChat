package com.pruebachat.appchat.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.pruebachat.appchat.data.repository.AuthenticationRepositoryImpl
import com.pruebachat.appchat.data.repository.DefaultLocationTrackerImpl
import com.pruebachat.appchat.domain.repository.AuthenticationRepository
import com.pruebachat.appchat.domain.repository.LocationTrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger Hilt que proporciona instancias de componentes necesarios para la autenticación y el seguimiento de ubicación.
 */

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    /**
     * Provee una instancia única de FirebaseAuth.
     *
     * @return Instancia de FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()



    /**
     * Provee una implementación única de AuthenticationRepository utilizando FirebaseAuth.
     *
     * @param auth Instancia de FirebaseAuth proporcionada por Dagger Hilt.
     * @return Implementación de AuthenticationRepository.
     */
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        auth: FirebaseAuth
    ): AuthenticationRepository = AuthenticationRepositoryImpl(auth)

    /**
     * Provee una instancia única de FusedLocationProviderClient utilizando LocationServices.
     *
     * @param application Aplicación Android para acceder al servicio de ubicación.
     * @return Instancia de FusedLocationProviderClient.
     */
    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    /**
     * Provee una implementación única de LocationTrackerRepository utilizando FusedLocationProviderClient.
     *
     * @param fusedLocationProviderClient Instancia de FusedLocationProviderClient proporcionada por Dagger Hilt.
     * @param application Aplicación Android para acceder al servicio de ubicación.
     * @return Implementación de LocationTrackerRepository.
     */


    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTrackerRepository = DefaultLocationTrackerImpl(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )

}