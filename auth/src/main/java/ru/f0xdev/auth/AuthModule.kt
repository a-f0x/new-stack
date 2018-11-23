package ru.f0xdev.auth

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.f0xdev.auth.ui.LoginPresenter

object AuthModule {
    val authModule = module {
        single<IAuthManager> { AuthManager(get("appContext")) }
        factory { Authenticator(get("authApi"), get("appContext")) }
        viewModel { LoginPresenter(get()) }
    }
}