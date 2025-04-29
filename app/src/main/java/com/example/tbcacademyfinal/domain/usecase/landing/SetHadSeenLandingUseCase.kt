package com.example.tbcacademyfinal.domain.usecase.landing

import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import javax.inject.Inject

class SetHadSeenLandingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(
        hasSeen: Boolean
    ) {
        dataStoreRepository.setHasSeenLanding(
            hasSeen = hasSeen
        )
    }

}