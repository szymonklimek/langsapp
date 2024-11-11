/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.klimek.langsapp.openapi.generated.user.profile.query

import com.klimek.langsapp.openapi.generated.user.profile.query.model.ProfileResponse

import com.klimek.langsapp.openapi.generated.user.profile.query.infrastructure.*
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.forms.formData
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json
import io.ktor.http.ParametersBuilder
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

open class ProfileApi : ApiClient {

    constructor(
        baseUrl: String = ApiClient.BASE_URL,
        httpClientEngine: HttpClientEngine? = null,
        httpClientConfig: ((HttpClientConfig<*>) -> Unit)? = null,
        jsonSerializer: Json = ApiClient.JSON_DEFAULT
    ) : super(baseUrl = baseUrl, httpClientEngine = httpClientEngine, httpClientConfig = httpClientConfig, jsonBlock = jsonSerializer)

    constructor(
        baseUrl: String,
        httpClient: HttpClient
    ): super(baseUrl = baseUrl, httpClient = httpClient)

    /**
     * Get current user profile
     * 
     * @param authorization Authorization token value
     * @param clientDeviceId Identifier of client device (optional)
     * @param clientDeviceSystemName Name of client device&#39;s operating system (optional)
     * @param clientDeviceSystemVersion Version of client device&#39;s operating system (optional)
     * @param clientDeviceModel Name of client&#39;s device model defined by the manufacturer (optional)
     * @param clientDeviceManufacturer Name of the manufacturer of client device (optional)
     * @param clientAppId Identifier of client application (optional)
     * @param clientAppVersion Version of client application (optional)
     * @return ProfileResponse
     */
    @Suppress("UNCHECKED_CAST")
    open suspend fun getCurrentUserProfile(authorization: kotlin.String, clientDeviceId: kotlin.String? = null, clientDeviceSystemName: kotlin.String? = null, clientDeviceSystemVersion: kotlin.String? = null, clientDeviceModel: kotlin.String? = null, clientDeviceManufacturer: kotlin.String? = null, clientAppId: kotlin.String? = null, clientAppVersion: kotlin.String? = null): HttpResponse<ProfileResponse> {

        val localVariableAuthNames = listOf<String>()

        val localVariableBody = 
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()
        authorization?.apply { localVariableHeaders["Authorization"] = this.toString() }
        clientDeviceId?.apply { localVariableHeaders["Client-Device-Id"] = this.toString() }
        clientDeviceSystemName?.apply { localVariableHeaders["Client-Device-System-Name"] = this.toString() }
        clientDeviceSystemVersion?.apply { localVariableHeaders["Client-Device-System-Version"] = this.toString() }
        clientDeviceModel?.apply { localVariableHeaders["Client-Device-Model"] = this.toString() }
        clientDeviceManufacturer?.apply { localVariableHeaders["Client-Device-Manufacturer"] = this.toString() }
        clientAppId?.apply { localVariableHeaders["Client-App-Id"] = this.toString() }
        clientAppVersion?.apply { localVariableHeaders["Client-App-Version"] = this.toString() }

        val localVariableConfig = RequestConfig<kotlin.Any?>(
            RequestMethod.GET,
            "/profile",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames
        ).wrap()
    }


}
