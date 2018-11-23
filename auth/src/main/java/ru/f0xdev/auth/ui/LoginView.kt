package ru.f0xdev.auth.ui

interface LoginView {
    fun showProgress(show: Boolean)
    fun showMessage(message: String)
    fun finish(token: String)
}