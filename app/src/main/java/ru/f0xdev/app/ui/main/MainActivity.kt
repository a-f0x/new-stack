package ru.f0xdev.app.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.f0xdev.app.R
import ru.f0xdev.auth.ui.LoginActivity
import ru.f0xdev.core.ui.BaseActivity

interface MainView

class MainActivity : BaseActivity<MainView, MainPresenter>() {
    override val presenter: MainPresenter  by viewModel { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStart.setOnClickListener { presenter.onAuthButtonClick() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let { intent ->
            when (requestCode) {
                LoginActivity.LOGIN_REQUEST_CODE -> {
                    val message =
                        if (resultCode == Activity.RESULT_OK)
                            "Token = ${intent.getStringExtra(LoginActivity.TOKEN_KEY)}"
                        else "Not authorized"
                    etResult.text = message
                }
            }
        }
    }
}