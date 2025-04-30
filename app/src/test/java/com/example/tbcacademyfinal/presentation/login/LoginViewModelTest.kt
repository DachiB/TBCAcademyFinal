package com.example.tbcacademyfinal.presentation.login

import com.example.tbcacademyfinal.common.Resource
import com.example.tbcacademyfinal.domain.usecase.auth.LoginUserUseCase
import com.example.tbcacademyfinal.domain.usecase.user.RememberUserUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidateEmailUseCase
import com.example.tbcacademyfinal.domain.usecase.validation.ValidatePasswordUseCase
import com.example.tbcacademyfinal.presentation.ui.auth.login.LoginIntent
import com.example.tbcacademyfinal.presentation.ui.auth.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private val validateEmail      = mockk<ValidateEmailUseCase>()
    private val validatePassword   = mockk<ValidatePasswordUseCase>()
    private val loginUser          = mockk<LoginUserUseCase>()
    private val rememberUser       = mockk<RememberUserUseCase>(relaxed = true)

    private lateinit var vm: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        vm = LoginViewModel(loginUser, validateEmail, validatePassword, rememberUser)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty email shows error and skips login`() = runBlocking {
        every { validateEmail("") } returns Resource.Error("Email cannot be empty.")
        vm.processIntent(LoginIntent.EmailChanged(""))
        vm.processIntent(LoginIntent.PasswordChanged("anything"))
        vm.processIntent(LoginIntent.LoginClicked)

        assertEquals("Email cannot be empty.", vm.state.errorMessage)
        coVerify(exactly = 0) { loginUser(any(), any()) }
    }

    @Test
    fun `failed login shows API error`() = runBlocking {
        every { validateEmail(any()) }   returns Resource.Success(Unit)
        every { validatePassword(any()) }returns Resource.Success(Unit)
        coEvery { loginUser(any(), any()) } returns flowOf(Resource.Error("Server down"))

        vm.processIntent(LoginIntent.EmailChanged("x@y.com"))
        vm.processIntent(LoginIntent.PasswordChanged("pw"))
        vm.processIntent(LoginIntent.LoginClicked)

        assertEquals("Server down", vm.state.errorMessage)
    }
}