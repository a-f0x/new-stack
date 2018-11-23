package ru.f0xdev.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import java.io.IOException

class Authenticator(private val api: IAuthApi, private val context: Context) : AbstractAccountAuthenticator(context) {

    companion object {
        const val KEY_LOGIN = "login_key"
        const val KEY_PASSWORD = "password_key"
        const val KEY_RESULT = "result_key"
        const val KEY_TOKEN = "token_key"

        const val KEY_ERROR_CODE = "error_code_key"
        const val KEY_ERROR_MESSAGE = "error_message_key"

        const val KEY_USER_DATA_ISSUED = "issued_user_data_key"
        const val KEY_USER_DATA_EXPIRED = "expired_user_data_key"
        const val KEY_USER_DATA_NAME = "name_user_data_key"
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        checkNotNull(options) { "Options is null!" }
        val result = Bundle()
        val login = options.getString(KEY_LOGIN)
        val password = options.getString(KEY_PASSWORD)
        checkNotNull(login) { "Login is null!" }
        checkNotNull(password) { "Password is null" }
        val am = AccountManager.get(context)
        val token = am.peekAuthToken(account, authTokenType)
        if (token.isNullOrEmpty()) {
            try {
                val user = api.login(login, password)
                am.setAuthToken(account, authTokenType, user.token)
                am.setUserData(account, KEY_USER_DATA_ISSUED, user.issued.toString())
                am.setUserData(account, KEY_USER_DATA_EXPIRED, user.expired.toString())
                am.setUserData(account, KEY_USER_DATA_NAME, user.name)
                result.putBoolean(KEY_RESULT, true)
                result.putString(KEY_TOKEN, user.token)
            } catch (e: Throwable) {
                result.putBoolean(KEY_RESULT, false)
                when (e) {
                    is AuthException -> {
                        result.putInt(KEY_ERROR_CODE, e.code)
                        result.putString(KEY_ERROR_MESSAGE, e.message)
                    }
                    is IOException -> {
                        result.putInt(
                            KEY_ERROR_CODE,
                            AuthException.CODE_NETWORK_ERROR
                        )
                        result.putString(KEY_ERROR_MESSAGE, e.message)
                    }
                }
            }
        }
        return result
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}