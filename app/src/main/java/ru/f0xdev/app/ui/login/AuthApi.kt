package ru.f0xdev.app.ui.login

import android.util.Log
import ru.f0xdev.auth.AuthException
import ru.f0xdev.auth.IAuthApi
import ru.f0xdev.auth.User
import java.io.IOException
import java.util.*

class AuthApi : IAuthApi {

    @Throws(AuthException::class)
    override fun login(login: String, password: String): User {
        Thread.sleep(2000)
        when (login) {
            "1" -> {
                throw IOException("Test io exception")
            }
            "2" -> {
                throw AuthException(401, "{\"message\":\"wrong credentials\"}")
            }
            "3" -> {
                throw AuthException(500, "{\"message\":\"server error\"}")
            }
            else -> {
                val date = Date().time
                return User("megatoken", date, (date + 10 * 60 * 1000), "User Name")
            }
        }
    }

    override fun refresh(access: String?, refresh: String) {
        Log.d("TEST-TAG", "authapi access $access, refresh $refresh")
    }
}