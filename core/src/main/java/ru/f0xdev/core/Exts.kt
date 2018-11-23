package ru.f0xdev.core

import java.io.IOException
import java.util.concurrent.TimeUnit

@Throws(IOException::class)
suspend fun <T> retryIO(block: suspend () -> T, retryTimeout: Long, tries: Int): T {

    var currTry = 0
    while (true) {
        try {
            return block()
        } catch (io: IOException) {
            if (currTry == tries)
                throw io
            currTry++
        }
        delay(retryTimeout)
    }
}


fun delay(timeout: Long) {
    TimeUnit.MILLISECONDS.sleep(timeout)
}