package com.klimek.langsapp.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.security.KeyFactory
import java.security.cert.CertificateFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.Base64


class JwtTokenAuthenticatorTest {

    @Test
    fun `test correct token authentication`() {
        val userId = "test-user-id"
        val token = JWT.create()
            .withSubject(userId)
            .sign(algorithm)
        val authResult = JwtTokenAuthenticator(algorithm).authenticate(Token(token))
        assertEquals(userId, authResult.getOrNull()?.userId)
    }

    @Test
    fun `test expired token authentication`() {
        val userId = "test-user-id"
        val token = JWT.create()
            .withSubject(userId)
            .withExpiresAt(Instant.now().minusSeconds(10))
            .sign(algorithm)
        val authResult = JwtTokenAuthenticator(algorithm).authenticate(Token(token))

        assertTrue(authResult.isLeft {
            it is AuthenticationError.TokenExpired
                    && it.errorMessage.contains("The Token has expired on ")
        }) {
            "Result is unexpectedly:  $authResult"
        }
    }

    @Test
    fun `test invalid signature token authentication`() {
        val exampleToken =
            """
                eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9
                .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0
                .NHVaYe26MbtOYhSKkoKYdFVomg4i8ZJd8_-RU8VNbftc4TSMb4bXP3l3YlNWACwyXPGffz5aXHc6lty1Y2t4SWRqGte
                ragsVdZufDn5BlnJl9pdR_kdVFUsra2rWKEofkZeIC4yWytE58sMIihvo9H1ScmmVwBcQP6XETqYd0aSHp1gOa9RdUPD
                voXQ5oqygTqVtxaDr6wUFKrKItgBMzWIdNZ6y7O9E0DhEPTbE9rfBo6KTFsHAZnMg4k68CDp2woYIaXbmYTWcvbzIuHO
                7_37GT79XdIwkm95QJ7hYC9RiwrV7mesbY4PAahERJawntho0my942XheVLmGwLMBkQ
            """
                .trimIndent()
                .replace("\n", "")
        val authResult = JwtTokenAuthenticator(algorithm).authenticate(Token(exampleToken))

        assertTrue(authResult.isLeft {
            it is AuthenticationError.InvalidToken
                    && it.errorMessage.contains(
                "The Token's Signature resulted invalid when verified using the Algorithm: SHA256withRSA"
            )
        }) {
            "Result is unexpectedly:  $authResult"
        }
    }

    @Test
    fun `test invalid configuration authentication`() {
        val exampleToken =
            """
                eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9
                .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0
                .NHVaYe26MbtOYhSKkoKYdFVomg4i8ZJd8_-RU8VNbftc4TSMb4bXP3l3YlNWACwyXPGffz5aXHc6lty1Y2t4SWRqGte
                ragsVdZufDn5BlnJl9pdR_kdVFUsra2rWKEofkZeIC4yWytE58sMIihvo9H1ScmmVwBcQP6XETqYd0aSHp1gOa9RdUPD
                voXQ5oqygTqVtxaDr6wUFKrKItgBMzWIdNZ6y7O9E0DhEPTbE9rfBo6KTFsHAZnMg4k68CDp2woYIaXbmYTWcvbzIuHO
                7_37GT79XdIwkm95QJ7hYC9RiwrV7mesbY4PAahERJawntho0my942XheVLmGwLMBkQ
            """
                .trimIndent()
                .replace("\n", "")
        val authResult = JwtTokenAuthenticator(
            Algorithm.RSA256(object : RSAKeyProvider {
                override fun getPublicKeyById(keyId: String?) = "Not a public key" as RSAPublicKey
                override fun getPrivateKey(): RSAPrivateKey = "Not a private key" as RSAPrivateKey
                override fun getPrivateKeyId() = testKeyId
            })
        ).authenticate(Token(exampleToken))

        assertTrue(authResult.isLeft {
            it is AuthenticationError.InvalidConfiguration
                    && it.errorMessage.contains(
                "class java.lang.String cannot be cast to class java.security.interfaces.RSAPublicKey"
            )
        }) {
            "Result is unexpectedly:  $authResult"
        }
    }

    companion object {

        private const val testKeyId = "d643a9d0f27715bee5c37df54d8482d7d13da37d"

        private val testPrivateKey = KeyFactory
            .getInstance("RSA")
            .generatePrivate(
                PKCS8EncodedKeySpec(
                    Base64.getDecoder().decode(
                        """
                            -----BEGIN PRIVATE KEY-----
                            MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL6fSVRYMElX2i1D
                            X3JNzy2RPg0Ldq0hEYjKj4RfLXPGujIS+d67pTrnlbWdI4xH7t+UMBhOog7k5M0O
                            SXPxF+/oMHMd1AC/EamzLrclOGUgmoPkL7OByDqYdJiTLdQUXGsAER9YMGklxPcs
                            WKp5p0klmX3wnflDnBeCrwwfBoDRAgMBAAECgYEAsyvFISJDQKzyxditviKkuY/r
                            YxeDVPfgI1NB0oojjs8b9DUh59k4VUWX0j0BTLnQLRZ8nlxKtvUumg7zu6bBd37F
                            iUTjNrbVtLNZwIsVpi0JdYca3Uyf9QxAi2ZXM4ehPz1/+HnitxxPjUBtHRT7OyTu
                            IAwoq2pDEzOvQy3JQIECQQDh6TgGvxPh0LptNVQAfdYhVEtE+zf5f0pvkBDVR0qE
                            yqwUpARCLfmXehzLJTgFdetm4OEekyME4empR6t+G9yDAkEA2ALXkpMl8MBJzMNP
                            1xrnBf3dlwZRWCevNhoGlTIZdSbLsQxVOhGonH6LlcEWryNV5enuGWuNsDzqdpWP
                            2eqVGwJBAJ6US5PQxXAaSQMoEBNYQdubhEqj3iLxS5sMkgRkytZ0Pl8u0x5xj2bZ
                            fSTMt4p9wASzjtMbjdV7j74YZTVc8WkCQQDVPukGAFJCO/IfPOlC14IePiJVh50i
                            Q/sKk+LPwuey6ZvIqv4wF2K3K44tRrNYLrAMC0ZMvgrgYyKr8rZSUOzlAkBZbsr8
                            NQR3gzb88xQw69qJ7/bjCvOcSpzFuKJQ4kYoPhdnu89qSaIR0uG517SCXQKx2klX
                            ciqytsixBy4qaZtS
                            -----END PRIVATE KEY-----
                        """
                            .trimIndent()
                            .replace("\n", "")
                            .replace("-----BEGIN PRIVATE KEY-----", "")
                            .replace("-----END PRIVATE KEY-----", "")
                    )
                )
            )

        private val testPublicKey = CertificateFactory
            .getInstance("X.509")
            .generateCertificate(
                Base64.getDecoder().decode(
                    """
                        -----BEGIN CERTIFICATE-----
                        MIICmjCCAgOgAwIBAgIBADANBgkqhkiG9w0BAQsFADBqMQswCQYDVQQGEwJkZTEP
                        MA0GA1UECAwGQmVybGluMREwDwYDVQQKDAhMYW5nc2FwcDERMA8GA1UEAwwIbGFu
                        Z3NhcHAxETAPBgNVBAcMCGxhbmdzYXBwMREwDwYDVQQLDAhsYW5nc2FwcDAeFw0y
                        MzA0MjAyMDUyMzZaFw0yNDA0MTkyMDUyMzZaMGoxCzAJBgNVBAYTAmRlMQ8wDQYD
                        VQQIDAZCZXJsaW4xETAPBgNVBAoMCExhbmdzYXBwMREwDwYDVQQDDAhsYW5nc2Fw
                        cDERMA8GA1UEBwwIbGFuZ3NhcHAxETAPBgNVBAsMCGxhbmdzYXBwMIGfMA0GCSqG
                        SIb3DQEBAQUAA4GNADCBiQKBgQC+n0lUWDBJV9otQ19yTc8tkT4NC3atIRGIyo+E
                        Xy1zxroyEvneu6U655W1nSOMR+7flDAYTqIO5OTNDklz8Rfv6DBzHdQAvxGpsy63
                        JThlIJqD5C+zgcg6mHSYky3UFFxrABEfWDBpJcT3LFiqeadJJZl98J35Q5wXgq8M
                        HwaA0QIDAQABo1AwTjAdBgNVHQ4EFgQU+OFoc2120E9G77uHPGsLnJh6lHMwHwYD
                        VR0jBBgwFoAU+OFoc2120E9G77uHPGsLnJh6lHMwDAYDVR0TBAUwAwEB/zANBgkq
                        hkiG9w0BAQsFAAOBgQBTeuf0+/hgtY2npm5odXXH0KOVyjMeBpwSsdIGmDmVUgKZ
                        FUU+ORobU3+Rn0XqFTxBDFT9pL9OngmqnRPrqEgWS96fUsL4aTKF3Sj00speDFfF
                        jUXPpxIKFN9FYXCf09fNH5xSdPDODOSar4Jc+KNihnAFJeHyfB9mGmSCYw3gow==
                        -----END CERTIFICATE-----
                    """
                        .trimIndent()
                        .replace("\n", "")
                        .replace("-----BEGIN CERTIFICATE-----", "")
                        .replace("-----END CERTIFICATE-----", "")
                ).inputStream()
            )
            .publicKey

        private val algorithm = Algorithm.RSA256(object : RSAKeyProvider {
            override fun getPublicKeyById(keyId: String?) = testPublicKey as RSAPublicKey
            override fun getPrivateKey(): RSAPrivateKey = testPrivateKey as RSAPrivateKey
            override fun getPrivateKeyId() = testKeyId
        })
    }
}
