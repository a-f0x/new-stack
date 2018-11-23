package ru.f0xdev.auth.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.f0xdev.auth.R
import ru.f0xdev.core.ui.BaseActivity


class LoginActivity : BaseActivity<LoginView, LoginPresenter>(), LoginView {

    companion object {
        const val LOGIN_REQUEST_CODE = 1099
        const val TOKEN_KEY = "login_key"
    }

    override val presenter: LoginPresenter by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            presenter.login(etLogin.text.toString(), etPassword.text.toString())
        }
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress.visibility = VISIBLE
            etPassword.isEnabled = false
            etLogin.isEnabled = false
        } else {
            progress.visibility = GONE
            etPassword.isEnabled = true
            etLogin.isEnabled = true
        }
    }

    override fun finish(token: String) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(TOKEN_KEY, token)
        })
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
