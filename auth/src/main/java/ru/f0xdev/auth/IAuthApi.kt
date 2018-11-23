package ru.f0xdev.auth


interface IAuthApi {
    @Throws(AuthException::class)
    fun login(login: String, password: String): User

    fun refresh(access: String?, refresh: String)

}

data class User(val token: String, val issued: Long, val expired: Long, val name: String?)