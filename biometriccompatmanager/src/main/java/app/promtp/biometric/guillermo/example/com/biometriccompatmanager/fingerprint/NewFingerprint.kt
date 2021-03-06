package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import java.security.Signature

/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 */

@RequiresApi(Build.VERSION_CODES.P)
class NewFingerprint(private val context: Context) : FingerprintBiometricParent, BiometricPrompt.AuthenticationCallback(), CypherProvider {

    override var cypherRequestor: CypherRequestor? = null

    @SuppressLint("MissingPermission")
    override fun showFingerprintDialog(dialogTitle: String, dialogSubtitle: String, dialogDescription: String, dialogNegativeButton: String) {
        val biometricPrompt = BiometricPrompt.Builder(context)
                .setTitle(dialogTitle)
                .setSubtitle(dialogSubtitle)
                .setDescription(dialogDescription)
                .setNegativeButton(dialogNegativeButton, context.mainExecutor,
                        DialogInterface.OnClickListener { _, _ ->
                            Log.i("-->", "Cancel button clicked")
                        })
                .build()

        biometricPrompt.authenticate(CancellationSignal(), context.mainExecutor, this)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        super.onAuthenticationError(errorCode, errString)
        Log.i("-->", "onAuthenticationError")
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        Log.i("-->", "onAuthenticationSucceeded")
        cypherRequestor?.invoke(result.cryptoObject.signature)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpCode, helpString)
        Log.i("-->", "onAuthenticationHelp")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Log.i("-->", "onAuthenticationFailed")
    }

    override fun provideCypher(cypherRequestor: CypherRequestor) {
        this.cypherRequestor = cypherRequestor
        // Get cypher -> Show fingerprintpo up
    }
}