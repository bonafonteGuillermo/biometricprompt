package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint


import android.content.Context
import android.util.Log
import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.utils.BiometricUtils

/**
 *
 * Created by Guillermo Bonafonte Criado on 28/11/2018.
 */
abstract class FingerprintFactory {

    companion object {
        fun create(context: Context): FingerprintBiometricParent {
            return if (BiometricUtils.isHardwareSupported(context) && BiometricUtils.isFingerprintAvailable(context)) {
                if (BiometricUtils.isAndroidPieAtLeast()) {
                    NewFingerprint(context)
                } else {
                    OldFingerprint(context)
                }
            } else {
                Log.i("-->", "FingerPrint not supported")
                OldFingerprint(context) //TODO CHANGE RETURN WHEN NOT
            }
        }
    }
}