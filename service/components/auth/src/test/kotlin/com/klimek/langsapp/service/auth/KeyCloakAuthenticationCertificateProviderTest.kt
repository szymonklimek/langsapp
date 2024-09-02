package com.klimek.langsapp.service.auth

import com.klimek.langsapp.service.auth.certificates.KeyCloakAuthenticationCertificateProvider
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KeyCloakAuthenticationCertificateProviderTest {

    private val testJson =
        """
            {
              "keys": [
                {
                  "kid": "nc7jI2yV1uulfOLb39DkdbZSjb0UqpezJiT7Rz69M28",
                  "kty": "RSA",
                  "alg": "RSA-OAEP",
                  "use": "enc",
                  "n": "1G1A3mUBcdcRvrruMSoZYiBGA2ZcuIDMGtzGEFL3ZvsjWzhOuzb1_SDtsKTllnfSBPbE_F6MGAh7uET9rX_lscSPwHQWTmW_vVEN-lKpLRd9Vrf9F9SjvmwaeHtwlib-XJOeRLGfpVzO7kVyL7etfgiG17m7ABy6nT1-PIm33sTFjXpFMgL29VoqF4B7e-Qdgy5ZwEMi6Hj3i9OrSAkGLNKPhBYswsqesIhRuWk_7s8-BfdJem2sIS2LcaN8QEd5we_gv-eoAZXx0eIAmGi-VEtUuy9CJh9s3XVhWmcCRGUlx2QBCuFyKu1Y0wZ_CcYEoSblde5imsBnPTEEsmF2hw",
                  "e": "AQAB",
                  "x5c": [
                    "MIICoTCCAYkCBgGQaRjGLzANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAlmb29kc25hcHAwHhcNMjQwNjMwMTIyMDQ5WhcNMzQwNjMwMTIyMjI5WjAUMRIwEAYDVQQDDAlmb29kc25hcHAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDUbUDeZQFx1xG+uu4xKhliIEYDZly4gMwa3MYQUvdm+yNbOE67NvX9IO2wpOWWd9IE9sT8XowYCHu4RP2tf+WxxI/AdBZOZb+9UQ36UqktF31Wt/0X1KO+bBp4e3CWJv5ck55EsZ+lXM7uRXIvt61+CIbXubsAHLqdPX48ibfexMWNekUyAvb1WioXgHt75B2DLlnAQyLoePeL06tICQYs0o+EFizCyp6wiFG5aT/uzz4F90l6bawhLYtxo3xAR3nB7+C/56gBlfHR4gCYaL5US1S7L0ImH2zddWFaZwJEZSXHZAEK4XIq7VjTBn8JxgShJuV17mKawGc9MQSyYXaHAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAEljLFjcI3iXCmAAcvs7fDNqPmI4UB0qbS0uO5ubGJh6hDT7urh1afb3j8ZlkdEM9bHAMyQs+/ywiLvTGx1lTCAEydj1gJ1VbBRvX9QYUzfjZyWvxUEdmx6ssZx9GJHAJAKUzdIaOf6WMiIIb5L2IDPPVlr3DTjFO7ZusU9v+w/Z3vyM1oG9GPJbYLwcE+aT1rlmsNAKhrVq0GxUbvXFYnknG0FTUVyh9X+Gk3hvFU11aeJILJXP4rb9mlvvj2Lp+RpKgyUHsRsVzXI+13JiFLx1Xn4zL8R4mNA64ep3jsHQCyWdoZ5iAfnpd9CrDhxUTfFLDbeDVwdPNRt+zlvdNIM="
                  ],
                  "x5t": "XlhsxCb4ievokGO8NZJX42-x2tI",
                  "x5t#S256": "6cmeuUeIkqiyHcqi4ATb59Q5ihqZ878jevpij633uHg"
                },
                {
                  "kid": "Y7QSfNi8H9oaQCinL2DMy-opHdO3itbkRBc_QpIYZCM",
                  "kty": "RSA",
                  "alg": "RS256",
                  "use": "sig",
                  "n": "oRgmLm_bUhnGSc2vWDGcZRx3V-lZ5T_5EAKq4m-TolubuxaQFcnEY14wwRIjUso3PVkmqJq0tGMa0kUHO4FIY39vX5rMnPs9LmLIwNMhg3M9e1Vm2cHjjtoeyMBDtOpHrHAGv0BQP1w-vFyshMvQnYDdO1miBXenH_THi-9O5AzuqNrrH_6y-J5L07kkxusGMS8g5hgeEnujXbUeVQluytRTRX5gRiYhoIDlCl4FwaSBG18M71TBkTOGNLuC0bQItL41zJ9V3ZTfzw82QARHiCtiHPykffVQnQ2wlx_mKNU7z99Ks3JGtbcA1Q1Xrbfh_Gyr-WimQjdaK5A5AL64YQ",
                  "e": "AQAB",
                  "x5c": [
                    "MIICoTCCAYkCBgGQaRjE1TANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAlmb29kc25hcHAwHhcNMjQwNjMwMTIyMDQ5WhcNMzQwNjMwMTIyMjI5WjAUMRIwEAYDVQQDDAlmb29kc25hcHAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQChGCYub9tSGcZJza9YMZxlHHdX6VnlP/kQAqrib5OiW5u7FpAVycRjXjDBEiNSyjc9WSaomrS0YxrSRQc7gUhjf29fmsyc+z0uYsjA0yGDcz17VWbZweOO2h7IwEO06kescAa/QFA/XD68XKyEy9CdgN07WaIFd6cf9MeL707kDO6o2usf/rL4nkvTuSTG6wYxLyDmGB4Se6NdtR5VCW7K1FNFfmBGJiGggOUKXgXBpIEbXwzvVMGRM4Y0u4LRtAi0vjXMn1XdlN/PDzZABEeIK2Ic/KR99VCdDbCXH+Yo1TvP30qzcka1twDVDVett+H8bKv5aKZCN1orkDkAvrhhAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAD3JuY8D7IjfCEv1xXl1YwdvPt9H8rmCezFqqVJg/Kbte5QXAsEfr4qTMTxgByBRr4W/TJbgIlcaMf8lRD26/sCAgBjNxkcm0yDvqbP9nW4BrYXydnY4sqc9LqaHDf6CSsugdKl0D8bspmzhg6u8ijqfgypQAze1uLqimKdz0QOsS8jSat0x09G8vBccbbJIIcgwpVHTgkmbGjJlOTMqcH1P9srGxA4sCtDkXIuZTrzS9CcMGcqUWEgnApK8mSVfdK8L9zWFyYOznKuizlXi+EDP8kFXmVk9sGXMYHSS9AEdmK/GZi0zOg9g/StIVB8e+asBdkZE7IR0SHSq5w47S7k="
                  ],
                  "x5t": "i1GvnBekKCaJmAw0vAvzJWGQctk",
                  "x5t#S256": "2LjHuVWAmSnU16UptNezqfcMDzFY5jhbLXOpp82iUzE"
                }
              ]
            }
        """.trimIndent()

    private var mockWebServer: MockWebServer? = null

    @Before
    fun before() {
        mockWebServer = MockWebServer().apply {
            start()
        }
    }

    @After
    fun after() {
        mockWebServer?.shutdown()
    }

    @Test
    fun `test retrieving certificate`() {
        mockWebServer?.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(testJson),
        )

        val provider = KeyCloakAuthenticationCertificateProvider(
            baseUrl = mockWebServer?.url("")!!.toString(),
            realm = "test",
        )

        assertEquals(
            "MIICoTCCAYkCBgGQaRjE1TANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAlmb29kc25hcHAwHhcNMjQwNjMwMTIyMDQ5WhcNMzQwNjMwMTIyMjI5WjAUMRIwEAYDVQQDDAlmb29kc25hcHAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQChGCYub9tSGcZJza9YMZxlHHdX6VnlP/kQAqrib5OiW5u7FpAVycRjXjDBEiNSyjc9WSaomrS0YxrSRQc7gUhjf29fmsyc+z0uYsjA0yGDcz17VWbZweOO2h7IwEO06kescAa/QFA/XD68XKyEy9CdgN07WaIFd6cf9MeL707kDO6o2usf/rL4nkvTuSTG6wYxLyDmGB4Se6NdtR5VCW7K1FNFfmBGJiGggOUKXgXBpIEbXwzvVMGRM4Y0u4LRtAi0vjXMn1XdlN/PDzZABEeIK2Ic/KR99VCdDbCXH+Yo1TvP30qzcka1twDVDVett+H8bKv5aKZCN1orkDkAvrhhAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAD3JuY8D7IjfCEv1xXl1YwdvPt9H8rmCezFqqVJg/Kbte5QXAsEfr4qTMTxgByBRr4W/TJbgIlcaMf8lRD26/sCAgBjNxkcm0yDvqbP9nW4BrYXydnY4sqc9LqaHDf6CSsugdKl0D8bspmzhg6u8ijqfgypQAze1uLqimKdz0QOsS8jSat0x09G8vBccbbJIIcgwpVHTgkmbGjJlOTMqcH1P9srGxA4sCtDkXIuZTrzS9CcMGcqUWEgnApK8mSVfdK8L9zWFyYOznKuizlXi+EDP8kFXmVk9sGXMYHSS9AEdmK/GZi0zOg9g/StIVB8e+asBdkZE7IR0SHSq5w47S7k=",
            provider.invoke("Y7QSfNi8H9oaQCinL2DMy-opHdO3itbkRBc_QpIYZCM"),
        )
    }
}
