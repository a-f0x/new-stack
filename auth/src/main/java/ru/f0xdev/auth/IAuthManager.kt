package ru.f0xdev.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf


interface IAuthManager {
    suspend fun login(login: String, password: String): String
    suspend fun refresh()
}

class AuthException(val code: Int, message: String?) : Exception(message) {
    companion object {
        const val CODE_NETWORK_ERROR = 1
        const val CODE_WRONG_CREDENTIALS = 401
        const val CODE_SERVER_ERROR = 500111
        const val CODE_AUTH_EXPIRED = 401111
    }
}

class AuthManager(private val context: Context) : IAuthManager {
    private val am = AccountManager.get(context)
    private val accountType = context.getString(R.string.account_type)
    private val accauntName = context.getString(R.string.account_name)

    override suspend fun login(login: String, password: String): String {
        val acc = getAccountOrCreate()
        am.setPassword(acc, password)
        val result = am.getAuthToken(
            acc,
            context.getString(R.string.token_type),
            bundleOf(Pair(Authenticator.KEY_LOGIN, login), Pair(Authenticator.KEY_PASSWORD, password)),
            false,
            null,
            null
        ).result
        val resultOk = result.getBoolean(Authenticator.KEY_RESULT)
        if (resultOk) {
            val token = result.getString(Authenticator.KEY_TOKEN)
            checkNotNull(token) { "Token is null!!" }
            return token
        } else {
            throw AuthException(
                result.getInt(Authenticator.KEY_ERROR_CODE),
                result.getString(Authenticator.KEY_ERROR_MESSAGE)
            )
        }
    }

    override suspend fun refresh() {
        Log.d("TEST-TAG", "authmanager refresh")
    }


    private fun getAccountOrCreate(): Account {
        val accs = am.getAccountsByType(accountType)
        if (accs.isEmpty()) {
            val acc = Account(accauntName, accountType)
            am.addAccountExplicitly(acc, null, Bundle.EMPTY)
            return acc
        }
        return accs[0]
    }


}