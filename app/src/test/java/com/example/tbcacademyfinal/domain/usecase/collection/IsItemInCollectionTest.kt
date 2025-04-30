package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class IsItemInCollectionUseCaseTest {

    private val repo = mockk<CollectionRepository>()
    private val useCase = IsItemInCollectionUseCase(repo)

    @Test
    fun `returns true when repository emits true`() = runBlocking {
        every { repo.isItemInCollection("p1") } returns flowOf(true)

        val result = useCase("p1").first()

        assertTrue(result)
    }

    @Test
    fun `returns false when repository emits false`() = runBlocking {
        every { repo.isItemInCollection("p2") } returns flowOf(false)

        val result = useCase("p2").first()

        assertFalse(result)
    }
}