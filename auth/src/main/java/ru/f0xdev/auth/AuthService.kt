package ru.f0xdev.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.koin.android.ext.android.inject

class AuthService : Service() {
    val authenticator: Authenticator by inject()

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}