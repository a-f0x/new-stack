package ru.f0xdev.app.ui.main

import android.app.Activity
import android.content.Intent
import ru.f0xdev.auth.ui.LoginActivity


interface IMainRouter {
    fun auth()
}

class MainRouter(private val routingContext: Activity) : IMainRouter {
    override fun auth() {
        val intent = Intent(routingContext, LoginActivity::class.java)
        routingContext.startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE)
    }
}