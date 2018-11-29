package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

/**
 *
 * Created by Guillermo Bonafonte Criado on 28/11/2018.
 */
class BiometricUtils {

    companion object {
        /**
         * Check if the device has Android PIE
         *
         * @return true if the device use Android Pie (28), false otherwise.
         * */
        fun isAndroidPieAtLeast(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
        }

        /**
         * Check if the device has fingerprint sensors.
         *
         * @return true if hardware is present and functional, false otherwise.
         * */
        @SuppressLint("MissingPermission")
        fun isHardwareSupported(context: Context): Boolean {
            val fingerprintManager = FingerprintManagerCompat.from(context)
            return fingerprintManager.isHardwareDetected
        }

        /**
         * Check if the user has registered at least one fingerprint on their device.
         *
         * @param Context
         * @return true if at least one fingerprint is enrolled, false otherwise
         * */
        @SuppressLint("MissingPermission")
        fun isFingerprintAvailable(context: Context): Boolean {
            val fingerprintManager = FingerprintManagerCompat.from(context)
            return fingerprintManager.hasEnrolledFingerprints()
        }
    }
}