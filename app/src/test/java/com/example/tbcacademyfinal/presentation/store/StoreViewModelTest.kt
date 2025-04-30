package com.example.tbcacademyfinal.presentation.store

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.model.Product
import com.example.tbcacademyfinal.domain.repository.ConnectivityObserver
import com.example.tbcacademyfinal.domain.usecase.collection.AddToCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.collection.IsItemInCollectionUseCase
import com.example.tbcacademyfinal.domain.usecase.network.ObserveNetworkStatusUseCase
import com.example.tbcacademyfinal.domain.usecase.products.GetProductsUseCase
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreIntent
import com.example.tbcacademyfinal.presentation.ui.main.store.StoreViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StoreViewModelTest {

    private val getProducts           = mockk<GetProductsUseCase>()
    private val observeNetwork = mockk<ObserveNetworkStatusUseCase>()

    private val addToCollection       = mockk<AddToCollectionUseCase>(relaxed = true)
    private val isInCollection        = mockk<IsItemInCollectionUseCase>(relaxed = true)

    private lateinit var vm: StoreViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        every { observeNetwork() } returns
                flowOf(ConnectivityObserver.Status.Available)

        val product = Product(
            id          = "p1",
            name        = "Sofa",
            description = "Desc",
            price       = 100.0,
            imageUrl    = "",
            category    = "Chairs",
            modelFile   = "m1.glb"
        )
        coEvery { getProducts() } returns flowOf(Resource.Success(listOf(product)))

        vm = StoreViewModel(getProducts, observeNetwork, addToCollection, isInCollection)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init with network available loads products into state`() = runBlocking {
        assertEquals(1, vm.state.allProducts.size)
        assertEquals(1, vm.state.currentProducts.size)
        assertTrue(!vm.state.isLoading)
        assertTrue(vm.state.error.isNullOrBlank())
    }

    @Test
    fun `search filters products by name`() = runBlocking {
        assertEquals(1, vm.state.currentProducts.size)

        vm.processIntent(StoreIntent.SearchQueryChanged("so"))
        Thread.sleep(600)
        runBlocking { /* wait */ }
        assertEquals(1, vm.state.currentProducts.size)

        vm.processIntent(StoreIntent.SearchQueryChanged("xyz"))
        Thread.sleep(600)
        runBlocking { /* wait */ }
        assertEquals(0, vm.state.currentProducts.size)
    }

}