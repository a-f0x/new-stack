package ru.f0xdev.core.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<View, Presenter : BasePresenter<View>> : AppCompatActivity() {
    abstract val presenter: Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachToPresenter()
    }

    private fun attachToPresenter() {

        presenter.attachView(this as View)
        lifecycle.addObserver(presenter)
    }
}