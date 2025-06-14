package com.example.tbcacademyfinal.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object SplashRoute : Routes()

    @Serializable
    data object LandingRoute : Routes()

    @Serializable
    data object AuthGraphRoute : Routes()

    @Serializable
    data object LoginRoute : Routes()

    @Serializable
    data object RegisterRoute : Routes()

    @Serializable
    data object MainGraphRoute : Routes()

    @Serializable
    data object MainContainerRoute : Routes()

    @Serializable
    data object StoreRoute : Routes()

    @Serializable
    data class DetailsRoute(val productId: String) : Routes()

    @Serializable
    data object CollectionRoute : Routes()

    @Serializable
    data object ArSceneRoute : Routes()

    @Serializable
    data object ProfileRoute : Routes()

    @Serializable
    data class ModelRoute(val productId: String) : Routes()

    @Serializable
    data object TutorialRoute : Routes()

    @Serializable
    data object TutorialGraphRoute : Routes()

}