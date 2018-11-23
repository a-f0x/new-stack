package ru.f0xdev.app.app

import android.app.Activity
import android.app.Application
import android.content.Context
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.f0xdev.app.ui.login.AuthApi
import ru.f0xdev.app.ui.main.MainPresenter
import ru.f0xdev.app.ui.main.MainRouter
import ru.f0xdev.auth.AuthModule
import ru.f0xdev.auth.IAuthApi

class App : Application() {

    private val appModule = module {
        single<Context>("appContext") { applicationContext }
        single<IAuthApi>("authApi") { AuthApi() }

        viewModel { (routingContext: Activity) -> MainPresenter(MainRouter(routingContext)) }
    }


    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, AuthModule.authModule))
    }
}