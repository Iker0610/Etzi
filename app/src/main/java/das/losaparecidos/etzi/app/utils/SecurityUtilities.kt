package das.losaparecidos.etzi.app.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class NoCryptographicKeyException : Exception()

interface CipherUtil {
    fun encryptData(keyAlias: String, data: String): String
    fun decryptData(keyAlias: String, encryptedDataWithIV: String): String
}

/*******************************************************************************
 ****                        AES Data Encryption Util                       ****
 *******************************************************************************/
/**
 * Implementation of [CipherUtil] that uses AES symmetric encryption.
 *
 * It uses Android's KeyStore Provider to get a KeyStore and generate and retrieve AES key for encryption.
 * Encryption is made with [Cipher] library, saving the initialization vector (IV) with the encrypted data.
 */
class AESCipher @Inject constructor() : CipherUtil {

    /*************************************************
     **              Static Attributes              **
     *************************************************/
    companion object {
        private const val IV_SEPARATOR = "]"
    }

    /*************************************************
     **                  Attributes                 **
     *************************************************/

    //-------   KeyStore Provider Related   --------//
    private val provider = "AndroidKeyStore"
    private val keyStore by lazy { KeyStore.getInstance(provider).apply { load(null) } }
    private val keyGenerator by lazy { KeyGenerator.getInstance(KEY_ALGORITHM_AES, provider) }

    //-------------   Cipher Related   -------------//
    private val cipher by lazy { Cipher.getInstance("AES/GCM/NoPadding") }


    /*************************************************
     **                   Methods                   **
     *************************************************/


    /*------------------------------------------------
    |             Key Management Methods             |
    ------------------------------------------------*/

    /**
     * Generates and returns an AES [SecretKey] and saves it in KeyStore with the given [keyAlias].
     */
    private fun generateSecretKey(keyAlias: String): SecretKey {
        return keyGenerator.apply {
            init(
                KeyGenParameterSpec
                    .Builder(keyAlias, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE_GCM)
                    .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )
        }.generateKey()
    }

    /**
     * Retrieves the [SecretKey] with the given [keyAlias] from Android's KeyStore
     */
    private fun getSecretKey(keyAlias: String): SecretKey = (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey


    /*------------------------------------------------
    |       Data Encryption/Decryption Methods       |
    ------------------------------------------------*/

    /**
     * Encrypts the given [data] with AES algorithm using the key saved with [keyAlias].
     */
    override fun encryptData(keyAlias: String, data: String): String {
        // Initialize the cipher in encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(keyAlias))

        // Encode IV and the encrypted data as an string
        val encodedIV = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        val encodedData = Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)

        // Return the concatenation of the  IV and the encoded data.
        return "$encodedIV$IV_SEPARATOR$encodedData"
    }

    /**
     * Given the [keyAlias] and encrypted data [iv] decrypts the given [encryptedData] and returns it as the original string.
     */
    @Throws(NoCryptographicKeyException::class)
    private fun decryptData(keyAlias: String, encryptedData: ByteArray, iv: ByteArray): String {
        try {
            // Initialize the cipher in decrypt mode
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(keyAlias), GCMParameterSpec(128, iv))
        } catch (e: NullPointerException) {
            throw NoCryptographicKeyException()
        }

        // Decrypt data and convert to string
        return String(cipher.doFinal(encryptedData))
    }

    /**
     * Given the [keyAlias] decrypts the given [encryptedDataWithIV] and returns it as the original string.
     *
     * @param encryptedDataWithIV - String that contains both the used IV and the encrypted data.
     */
    @Throws(NoCryptographicKeyException::class)
    override fun decryptData(keyAlias: String, encryptedDataWithIV: String): String {
        // Split the encrypted string to retrieve the used IV and the encrypted data.
        val split = encryptedDataWithIV.split(IV_SEPARATOR.toRegex())
        if (split.size != 2) throw IllegalArgumentException("Passed data is not valid. It does not define a valid IV.")

        // Decode both strings to get a ByteArray
        val iv = Base64.decode(split[0], Base64.DEFAULT)
        val encryptedDataBytes = Base64.decode(split[1], Base64.DEFAULT)

        // Return decrypted data
        return decryptData(keyAlias, encryptedDataBytes, iv)
    }
}