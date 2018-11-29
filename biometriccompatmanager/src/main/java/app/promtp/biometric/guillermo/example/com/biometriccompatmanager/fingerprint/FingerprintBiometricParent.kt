package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.Biometric
import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.callbacks.FingerprintAuthenticateCallback

/**
 *
 * Created by Guillermo Bonafonte Criado on 28/11/2018.
 */
interface FingerprintBiometricParent : Biometric, FingerprintAuthenticateCallback{
    fun showFingerprintDialog(
            dialogTitle: String,
            dialogSubtitle: String,
            dialogDescription: String,
            dialogNegativeButton: String
    )
}