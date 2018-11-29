package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.callbacks

import android.hardware.biometrics.BiometricPrompt
import android.util.Log

/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 * 2018 © Cognizant Technology Solutions
 */
interface FingerprintAuthenticateCallback {
    fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
    fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?)
    fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
    fun onAuthenticationFailed()
}