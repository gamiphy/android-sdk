package com.gamiphy.library.utils

import android.util.Log
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Util class to generate hmac for the given data with the given key using HMAC-SHA256
 */
object HashUtil {
    private const val HMAC_SHA_256 = "HmacSHA256"
    /**
     * Generates a HMAC for the given [data] with the given [key] using HMAC-SHA256.
     *
     * @returns HMAC
     */
    fun createHmacKey(data: String, key: String): String {
        val hmacKey = SecretKeySpec((key).toByteArray(Charsets.UTF_8), HMAC_SHA_256)
        val mac = Mac.getInstance(HMAC_SHA_256)
        mac.init(hmacKey)

        val bytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        bytes.toString()

        val hash = StringBuffer()
        bytes.forEach { byte ->
            val hex: String = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) {
                hash.append('0')
            }
            hash.append(hex)
        }
        return hash.toString()
    }
}