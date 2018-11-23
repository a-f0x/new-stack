package ru.f0xdev.auth.ui

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.f0xdev.auth.AuthException
import ru.f0xdev.auth.IAuthManager
import ru.f0xdev.core.ui.BasePresenter


class LoginPresenter(private val authManager: IAuthManager) : BasePresenter<LoginView>() {

    /**
     *
     * Если внутри лямбды вызывается корутинбилдер в нашем случае [GlobalScope.async]
     * то эта лямбда автоматом превращается в суспенд функцию
     * */
    fun login(login: String, password: String) {
        view?.showProgress(true)
        launchOnUI("") {
            val job = GlobalScope.async { authManager.login(login, password) }
            try {
                addJob(job)
                val token = job.await()
                view?.finish(token)
            } catch (t: Throwable) {
                t.printStackTrace()
                if (t is AuthException) {
                    view?.showProgress(false)
                    view?.showMessage(t.message ?: "empty message")
                }
            }
        }
    }


}