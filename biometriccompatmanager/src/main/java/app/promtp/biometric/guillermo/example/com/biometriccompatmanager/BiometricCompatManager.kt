package app.promtp.biometric.guillermo.example.com.biometriccompatmanager

import android.content.Context
import android.security.keystore.KeyProperties
import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint.FingerprintFactory


/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 * 2018 © Cognizant Technology Solutions
 */
class BiometricCompatManager(private val context: Context, private var purpose: Int) {
    fun authenticateFingerprint(dialogTitle: String,
                                dialogSubtitle: String,
                                dialogDescription: String,
                                dialogNegativeButton: String) {
        val biometric = FingerprintFactory.create(context)
        biometric?.showFingerprintDialog(dialogTitle,dialogSubtitle,dialogDescription,dialogNegativeButton)
    }
}