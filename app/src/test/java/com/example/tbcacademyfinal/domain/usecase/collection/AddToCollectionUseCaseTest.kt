package com.example.tbcacademyfinal.domain.usecase.collection

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.CollectionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertIs

class AddToCollectionUseCaseTest {

    private val repo = mockk<CollectionRepository>()
    private val useCase = AddToCollectionUseCase(repo)

    @Test
    fun `returns error when modelFile is blank`() = runBlocking {
        val product = Product(
            id = "1",
            name = "Item",
            description = "Desc",
            price = 10.0,
            imageUrl = "",
            category = "Cat",
            modelFile = ""
        )

        val result = useCase(product)

        assertIs<Resource.Error>(result)
        assertEquals(
            "Product cannot be added to collection without a model file.",
            result.message
        )
    }

    @Test
    fun `delegates to repository when modelFile not blank`(): Unit = runBlocking {
        val product = Product(
            id = "2",
            name = "Item",
            description = "Desc",
            price = 10.0,
            imageUrl = "",
            category = "Cat",
            modelFile = "model.glb"
        )
        // stub repository to succeed
        coEvery { repo.addItemToCollection(product) } returns Resource.Success(Unit)

        val result = useCase(product)

        assertIs<Resource.Success<Unit>>(result)
    }
}