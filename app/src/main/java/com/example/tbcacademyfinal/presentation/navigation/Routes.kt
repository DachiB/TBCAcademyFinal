package com.example.tbcacademyfinal.presentation.navigation

import kotlinx.serialization.Serializable

// Use objects for destinations without parameters
@Serializable object SplashRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object HomeRoute // Container for bottom nav screens
@Serializable object StoreRoute // Technically part of Home's NavHost
@Serializable object CollectionRoute // Technically part of Home's NavHost
@Serializable object ProfileRoute // Technically part of Home's NavHost

// Use data classes for destinations requiring parameters
@Serializable data class DetailsRoute(val productId: String) // Example: Pass product ID

// You might have nested graphs, e.g., for the onboarding flow vs the main app flow
@Serializable object AuthGraph // Could represent the Login/Register flow
@Serializable object MainGraph // Could represent the flow after login (Home etc.)