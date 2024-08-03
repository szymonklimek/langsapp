package com.klimek.langsapp.service.auth

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.klimek.langsapp.auth.jwt.JwtTokenAuthenticator
import java.security.KeyFactory
import java.security.cert.CertificateFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

class FirebaseTokenAuthenticator(
    private val x509Certificate: String,
    private val privateKey: String,
    private val privateKeyId: String,
) : JwtTokenAuthenticator(
    algorithm = Algorithm.RSA256(object : RSAKeyProvider {
        override fun getPublicKeyById(keyId: String?) = CertificateFactory
            .getInstance("X.509")
            .generateCertificate(Base64.getDecoder().decode(x509Certificate).inputStream())
            .publicKey as RSAPublicKey
        override fun getPrivateKey(): RSAPrivateKey =
            KeyFactory
                .getInstance("RSA")
                .generatePrivate(
                    PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)),
                ) as RSAPrivateKey
        override fun getPrivateKeyId() = privateKeyId
    }),
)
