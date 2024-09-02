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
import java.util.Date

class JwtTokenAuthenticatorTest {

    @Test
    fun `test correct token authentication`() {
        val userId = "test-user-id"
        val token = JWT.create()
            .withIssuer("test-issuer")
            .withSubject(userId)
            .sign(signAlgorithm)
        val authResult = JwtTokenAuthenticator(
            validTokenIssuer = "test-issuer",
            x509CertificateProvider = { testPublicKeyString },
        )
            .authenticate(Token(token))

        assertEquals(userId, authResult.getOrNull()?.userId)
    }

    @Test
    fun `test expired token authentication`() {
        val userId = "test-user-id"
        val token = JWT.create()
            .withIssuer("test-issuer")
            .withSubject(userId)
            .withExpiresAt(Date.from(Instant.now().minusSeconds(200)))
            .sign(signAlgorithm)
        val authResult = JwtTokenAuthenticator(
            validTokenIssuer = "test-issuer",
            x509CertificateProvider = { testPublicKeyString },
        )
            .authenticate(Token(token))

        assertTrue(
            authResult.isLeft {
                it is AuthenticationError.TokenExpired &&
                    it.errorMessage.contains("The Token has expired on ")
            },
        ) {
            "Result is unexpectedly:  $authResult"
        }
    }

    @Test
    fun `test expired token authentication works fine when skipping verification`() {
        val userId = "test-user-id"
        val token = JWT.create()
            .withIssuer("test-issuer")
            .withSubject(userId)
            .withExpiresAt(Date.from(Instant.now().minusSeconds(10)))
            .sign(signAlgorithm)

        val authResult = JwtTokenAuthenticator(
            validTokenIssuer = "test-issuer",
            x509CertificateProvider = { testPublicKeyString },
        )
            .authenticate(Token(token), skipVerification = true)

        assertEquals(userId, authResult.getOrNull()?.userId)
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
        val authResult = JwtTokenAuthenticator(
            validTokenIssuer = "test-issuer",
            x509CertificateProvider = { testPublicKeyString },
        )
            .authenticate(Token(exampleToken))

        assertTrue(
            authResult.isLeft {
                it is AuthenticationError.InvalidToken &&
                    it.toString().contains(
                        "InvalidToken(errorMessage=Invalid token issuer. Expected: test-issuer, but was: null)",
                    )
            },
        ) {
            "Result is unexpectedly:  $authResult"
        }
    }

    @Test
    fun `test token verification for expired token`() {
        val token =
            "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZN1FTZk5pOEg5b2FRQ2luTDJETXktb3BIZE8zaXRia1JCY19RcElZWkNNIn0.eyJleHAiOjE3MjAwODk2NjksImlhdCI6MTcyMDA4OTM2OSwiYXV0aF90aW1lIjoxNzIwMDg5MzY4LCJqdGkiOiJiOGEzMjYyYi00NTcxLTQzOGUtYjE5NS04Njg2NWZlZTU2OTEiLCJpc3MiOiJodHRwczovL2F1dGguc2tsaW1lay5jb20vcmVhbG1zL2Zvb2RzbmFwcCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI4NWQ4ZDk2MS04NDAyLTQ3MTMtYWI0NC1kMTY0NDY0Mjc4ODMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJmb29kc25hcHBfYW5kcm9pZF9hcHAiLCJzaWQiOiI2NGZlY2VkOS0yMjFkLTRkMDMtYWE5MS0yNGE4MGM0ZjU5MmYiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1mb29kc25hcHAiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic3p5bW9uMmtsaW1la0BnbWFpbC5jb20iLCJlbWFpbCI6InN6eW1vbjJrbGltZWtAZ21haWwuY29tIn0.cxBYCfVAGC0SQfAyiyxp_vq9s-aVUcZliATT8SjQcs4pYz_zNOwejco_5chKO9LXEsJdwhdtNyYdDoIyAbWICtQejktDiI9-qBgE-Ei_eTuTLQWz8-xoLYXeY-LlEM7Ff71Snwii2xvNiwNx3EFDDaLCsT-sr7k3cf-fK4frujOjr01Wl0Ipgn6OxpimfDI4ATTRZ6cBe1FlxVKOl4yv4ozSp5V5bMiQLiayCK4C9Wt7o8iOBekMQN9VN-Lze4gIHlqQlUlySbXZqOmDoDKbPqMe9GEbPYsUP13S_xxBWIF8gRcayw5A1dduLp1MJemxJJ9w53VG6uXjR5w2-X-iTg"
        val authResult = JwtTokenAuthenticator(
            validTokenIssuer = "https://auth.sklimek.com/realms/foodsnapp",
            x509CertificateProvider = {
                if (it == "Y7QSfNi8H9oaQCinL2DMy-opHdO3itbkRBc_QpIYZCM") {
                    "MIICoTCCAYkCBgGQaRjE1TANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAlmb29kc25hcHAwHhcNMjQwNjMwMTIyMDQ5WhcNMzQwNjMwMTIyMjI5WjAUMRIwEAYDVQQDDAlmb29kc25hcHAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQChGCYub9tSGcZJza9YMZxlHHdX6VnlP/kQAqrib5OiW5u7FpAVycRjXjDBEiNSyjc9WSaomrS0YxrSRQc7gUhjf29fmsyc+z0uYsjA0yGDcz17VWbZweOO2h7IwEO06kescAa/QFA/XD68XKyEy9CdgN07WaIFd6cf9MeL707kDO6o2usf/rL4nkvTuSTG6wYxLyDmGB4Se6NdtR5VCW7K1FNFfmBGJiGggOUKXgXBpIEbXwzvVMGRM4Y0u4LRtAi0vjXMn1XdlN/PDzZABEeIK2Ic/KR99VCdDbCXH+Yo1TvP30qzcka1twDVDVett+H8bKv5aKZCN1orkDkAvrhhAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAD3JuY8D7IjfCEv1xXl1YwdvPt9H8rmCezFqqVJg/Kbte5QXAsEfr4qTMTxgByBRr4W/TJbgIlcaMf8lRD26/sCAgBjNxkcm0yDvqbP9nW4BrYXydnY4sqc9LqaHDf6CSsugdKl0D8bspmzhg6u8ijqfgypQAze1uLqimKdz0QOsS8jSat0x09G8vBccbbJIIcgwpVHTgkmbGjJlOTMqcH1P9srGxA4sCtDkXIuZTrzS9CcMGcqUWEgnApK8mSVfdK8L9zWFyYOznKuizlXi+EDP8kFXmVk9sGXMYHSS9AEdmK/GZi0zOg9g/StIVB8e+asBdkZE7IR0SHSq5w47S7k="
                } else {
                    ""
                }
            },
        ).authenticate(Token(token))

        assertTrue(
            authResult.isLeft {
                it is AuthenticationError.TokenExpired &&
                    it.toString().contains("The Token has expired on")
            },
        ) {
            "Result is unexpectedly:  $authResult"
        }
    }

    companion object {

        private val testPrivateKeyString =
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

        private val testPrivateKey = KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(testPrivateKeyString)))

        private val testPublicKeyString =
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

        private val testPublicKey = CertificateFactory.getInstance("X.509")
            .generateCertificate(Base64.getDecoder().decode(testPublicKeyString).inputStream())
            .publicKey

        private val signAlgorithm = Algorithm.RSA256(object : RSAKeyProvider {
            override fun getPublicKeyById(keyId: String?) = testPublicKey as RSAPublicKey
            override fun getPrivateKey(): RSAPrivateKey = testPrivateKey as RSAPrivateKey
            override fun getPrivateKeyId() = "d643a9d0f27715bee5c37df54d8482d7d13da37d"
        })
    }
}
