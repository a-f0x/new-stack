package ru.f0xdev.app.ui.main

import ru.f0xdev.core.ui.BasePresenter

class MainPresenter(private val mainRouter: IMainRouter) : BasePresenter<MainView>() {

    fun onAuthButtonClick() {
        mainRouter.auth()
    }
}