package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import android.content.Context
import android.util.Log
import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.utils.BiometricUtils
import java.security.Signature

/**
 *
 * Created by Guillermo Bonafonte Criado on 28/11/2018.
 */
abstract class FingerprintFactory {

    companion object {
        fun create(context: Context) : FingerprintBiometricParent? {
//            var signarute : Signature? = null
            if (BiometricUtils.isHardwareSupported(context) && BiometricUtils.isFingerprintAvailable(context)) {
                /*val cypherProvider: CypherProvider =
                        if (BiometricUtils.isAndroidPieAtLeast())
                            NewFingerprint(context)
                        else OldFingerprint(context)
                cypherProvider.provideCypher {
                    Log.i("-->", it.toString())
                    signarute = it
                }
                return signarute*/

               return if (BiometricUtils.isAndroidPieAtLeast())
                    NewFingerprint(context)
                else OldFingerprint(context)
            } else {
                throw IllegalStateException("Fingerprint is not supported")
            }
        }
    }
}