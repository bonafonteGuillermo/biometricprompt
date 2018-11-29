package app.promtp.biometric.guillermo.example.com.biometricapplication

import android.os.Bundle
import android.security.keystore.KeyProperties
import androidx.appcompat.app.AppCompatActivity
import app.promtp.biometric.guillermo.example.com.biometriccompatmanager.BiometricCompatManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img_fingerprint.setOnClickListener {
            val biometricCompatManager = BiometricCompatManager(this, KeyProperties.PURPOSE_ENCRYPT)
            biometricCompatManager.authenticateFingerprint(
                    getString(R.string.dialog_title),
                    getString(R.string.dialog_subtitle),
                    getString(R.string.dialog_description),
                    getString(R.string.dialog_negative_text)
            )
        }
    }
}