package app.promtp.biometric.guillermo.example.com.biometriccompatmanager.fingerprint

import java.security.Signature

/**
 *
 * Created by Guillermo Bonafonte Criado on 29/11/2018.
 */
typealias CypherRequestor = (Signature) ->  Unit
interface CypherProvider {
    var cypherRequestor: CypherRequestor?
    fun provideCypher(cypherRequestor: CypherRequestor)
}