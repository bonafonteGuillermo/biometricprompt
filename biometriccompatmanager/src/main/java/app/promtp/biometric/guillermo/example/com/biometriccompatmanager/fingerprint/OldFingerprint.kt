package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 * 2018 © Cognizant Technology Solutions
 */
class OldFingerprint(private val context: Context) : FingerprintBiometricParent, FingerprintManagerCompat.AuthenticationCallback() {


    private var KEY_NAME = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManagerCompat.CryptoObject
    private lateinit var fingerprintManager: FingerprintManagerCompat
    private var keyguardManager: KeyguardManager? = null
    private var cancellationSignal: CancellationSignal? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun showFingerprintDialog(dialogTitle: String, dialogSubtitle: String, dialogDescription: String, dialogNegativeButton: String) {

        keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = context.getSystemService(FINGERPRINT_SERVICE) as FingerprintManagerCompat

        try {
            generateKey()
        } catch (e: FingerprintException) {
            e.printStackTrace()
        }

        if (initCipher()) {
            cryptoObject = FingerprintManagerCompat.CryptoObject(cipher)
            startAuth(fingerprintManager, cryptoObject)
        }
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        super.onAuthenticationError(errMsgId, errString)
        Log.i("-->", "onAuthenticationError")
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)
        Log.i("-->", "onAuthenticationSucceeded")
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpMsgId, helpString)
        Log.i("-->", "onAuthenticationHelp")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Log.i("-->", "onAuthenticationFailed")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            keyStore.load(null)
            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())

            keyGenerator.generateKey()

        } catch (e: Exception) {
            e.printStackTrace()
            throw FingerprintException(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: Exception) {
            e.printStackTrace()
            throw FingerprintException(e)
        }

        return try {
            keyStore.load(
                    null)
            val key = keyStore.getKey(KEY_NAME, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: KeyPermanentlyInvalidatedException) {
            false
        } catch (e: Exception) {
            e.printStackTrace()
            throw FingerprintException(e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startAuth(manager: FingerprintManagerCompat, cryptoObject: FingerprintManagerCompat.CryptoObject) {
        cancellationSignal = CancellationSignal()
        manager.authenticate(cryptoObject, 0,cancellationSignal, this, null)
    }

    private inner class FingerprintException(e: Exception) : Exception(e)
}