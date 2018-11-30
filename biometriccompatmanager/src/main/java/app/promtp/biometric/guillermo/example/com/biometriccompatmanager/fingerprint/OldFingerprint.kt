package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 * 2018 © Cognizant Technology Solutions
 */
class OldFingerprint(private val context: Context) : FingerprintBiometricParent, FingerprintManagerCompat.AuthenticationCallback(), CypherProvider {

    override var cypherRequestor: CypherRequestor? = null

    private var KEY_NAME = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private val cryptoObject: FingerprintManagerCompat.CryptoObject by lazy { FingerprintManagerCompat.CryptoObject(cipher) }
    private val keyGenerator: KeyGenerator by lazy { KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore") }
    private val fingerprintManager: FingerprintManagerCompat by lazy { FingerprintManagerCompat.from(context) }
    private val keyguardManager: KeyguardManager by lazy { context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager } //TODO maybe not needed in order to check lockscreen security in your device's Settings
    private val cancellationSignal: CancellationSignal by lazy { CancellationSignal() }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun showFingerprintDialog(dialogTitle: String, dialogSubtitle: String, dialogDescription: String, dialogNegativeButton: String) {
        try {
            generateKey()
        } catch (e: FingerprintException) {
            e.printStackTrace()
        }

        if (initCipher()) {
            startAuth(fingerprintManager, cryptoObject)
        }
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        Log.i("-->", "onAuthenticationError")
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
        Log.i("-->", "onAuthenticationSucceeded")
        cypherRequestor?.invoke(result.cryptoObject.signature!!)
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        Log.i("-->", "onAuthenticationHelp")
    }

    override fun onAuthenticationFailed() {
        Log.i("-->", "onAuthenticationFailed")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore")
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
        manager.authenticate(cryptoObject, 0, cancellationSignal, this, null)
    }

    private inner class FingerprintException(e: Exception) : Exception(e)

    override fun provideCypher(cypherRequestor: CypherRequestor) {
        this.cypherRequestor = cypherRequestor
        // Get cypher -> Show fingerprintpo up
    }
}