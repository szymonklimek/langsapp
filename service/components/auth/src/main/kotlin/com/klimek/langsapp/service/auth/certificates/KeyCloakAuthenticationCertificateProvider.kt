package com.klimek.langsapp.service.auth.certificates

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

typealias CertificateId = String
typealias Certificate = String

class KeyCloakAuthenticationCertificateProvider(
    val baseUrl: String,
    val realm: String,
) {
    private var certificates: Map<CertificateId, Certificate> = getCertificates()
    private val refreshPeriodMs = TimeUnit.HOURS.toMillis(12)

    init {
        timer(
            name = "keycloak_auth_certificates_refresh_timer",
            startAt = Date.from(Instant.now().plusMillis(refreshPeriodMs)),
            period = refreshPeriodMs,
        ) {
            refreshCertificates()
        }
    }

    operator fun invoke(certificateId: CertificateId): Certificate =
        if (certificates.isEmpty()) {
            error("Empty list of certificates")
        } else {
            certificates[certificateId] ?: "Invalid key id"
        }

    private fun refreshCertificates() {
        log.info("Refreshing certificates. Current: $certificates")
        certificates = getCertificates()
        log.info("Refreshing certificates finished. Updated: $certificates")
    }

    private fun getCertificates(): Map<CertificateId, Certificate> =
        WebClient.create("$baseUrl/realms/$realm/protocol/openid-connect/certs")
            .also {
                log.info("Downloading certificates...")
            }
            .get()
            .retrieve()
            .toEntity(KeysResponse::class.java)
            .doOnError {
                log.error("Failed to download certificates. Reason: $it")
            }
            .block()
            ?.body
            ?.keys
            ?.associate {
                Pair(
                    first = it.kid,
                    second = it.x5c.first(),
                )
            }
            ?.onEach { (kid, certificate) ->
                log.info("Loaded certificate kid: $kid, content: $certificate")
            }
            ?: emptyMap()

    companion object {
        private val log = LoggerFactory.getLogger(KeyCloakAuthenticationCertificateProvider::class.java)
    }
}

data class KeysResponse(
    @get:JsonProperty("keys", required = true) val keys: List<JWK>,
)

data class JWK(
    @get:JsonProperty("kid", required = true) val kid: String,
    @get:JsonProperty("x5c", required = true) val x5c: List<String>,
)
