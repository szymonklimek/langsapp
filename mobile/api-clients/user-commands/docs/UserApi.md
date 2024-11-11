# UserApi

All URIs are relative to *https://api.langsapp.net*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createUser**](UserApi.md#createUser) | **POST** /user | Creates user |
| [**updateUser**](UserApi.md#updateUser) | **PATCH** /user | Updates properties of previously created user |


<a id="createUser"></a>
# **createUser**
> createUser(authorization, createUserRequest, clientDeviceId, clientDeviceSystemName, clientDeviceSystemVersion, clientDeviceModel, clientDeviceManufacturer, clientAppId, clientAppVersion)

Creates user

### Example
```kotlin
// Import classes:
//import com.klimek.langsapp.openapi.generated.user.commands.infrastructure.*
//import com.klimek.langsapp.openapi.generated.user.commands.model.*

val apiInstance = UserApi()
val authorization : kotlin.String = Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c // kotlin.String | Authorization token value
val createUserRequest : CreateUserRequest =  // CreateUserRequest | User to create
val clientDeviceId : kotlin.String = 5c4c26c0-871c-4d14-8a20-08051c530e1f // kotlin.String | Identifier of client device
val clientDeviceSystemName : kotlin.String = android // kotlin.String | Name of client device's operating system
val clientDeviceSystemVersion : kotlin.String = 7.1.2 // kotlin.String | Version of client device's operating system
val clientDeviceModel : kotlin.String = sm-g960f // kotlin.String | Name of client's device model defined by the manufacturer
val clientDeviceManufacturer : kotlin.String = Samsung // kotlin.String | Name of the manufacturer of client device
val clientAppId : kotlin.String = com.example.android // kotlin.String | Identifier of client application
val clientAppVersion : kotlin.String = 1.1.2 (123) // kotlin.String | Version of client application
try {
    apiInstance.createUser(authorization, createUserRequest, clientDeviceId, clientDeviceSystemName, clientDeviceSystemVersion, clientDeviceModel, clientDeviceManufacturer, clientAppId, clientAppVersion)
} catch (e: ClientException) {
    println("4xx response calling UserApi#createUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserApi#createUser")
    e.printStackTrace()
}
```

### Parameters
| **authorization** | **kotlin.String**| Authorization token value | |
| **createUserRequest** | [**CreateUserRequest**](CreateUserRequest.md)| User to create | |
| **clientDeviceId** | **kotlin.String**| Identifier of client device | [optional] |
| **clientDeviceSystemName** | **kotlin.String**| Name of client device&#39;s operating system | [optional] |
| **clientDeviceSystemVersion** | **kotlin.String**| Version of client device&#39;s operating system | [optional] |
| **clientDeviceModel** | **kotlin.String**| Name of client&#39;s device model defined by the manufacturer | [optional] |
| **clientDeviceManufacturer** | **kotlin.String**| Name of the manufacturer of client device | [optional] |
| **clientAppId** | **kotlin.String**| Identifier of client application | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **clientAppVersion** | **kotlin.String**| Version of client application | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="updateUser"></a>
# **updateUser**
> updateUser(authorization, updateUserRequest, clientDeviceId, clientDeviceSystemName, clientDeviceSystemVersion, clientDeviceModel, clientDeviceManufacturer, clientAppId, clientAppVersion)

Updates properties of previously created user

### Example
```kotlin
// Import classes:
//import com.klimek.langsapp.openapi.generated.user.commands.infrastructure.*
//import com.klimek.langsapp.openapi.generated.user.commands.model.*

val apiInstance = UserApi()
val authorization : kotlin.String = Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c // kotlin.String | Authorization token value
val updateUserRequest : UpdateUserRequest =  // UpdateUserRequest | Updated user object
val clientDeviceId : kotlin.String = 5c4c26c0-871c-4d14-8a20-08051c530e1f // kotlin.String | Identifier of client device
val clientDeviceSystemName : kotlin.String = android // kotlin.String | Name of client device's operating system
val clientDeviceSystemVersion : kotlin.String = 7.1.2 // kotlin.String | Version of client device's operating system
val clientDeviceModel : kotlin.String = sm-g960f // kotlin.String | Name of client's device model defined by the manufacturer
val clientDeviceManufacturer : kotlin.String = Samsung // kotlin.String | Name of the manufacturer of client device
val clientAppId : kotlin.String = com.example.android // kotlin.String | Identifier of client application
val clientAppVersion : kotlin.String = 1.1.2 (123) // kotlin.String | Version of client application
try {
    apiInstance.updateUser(authorization, updateUserRequest, clientDeviceId, clientDeviceSystemName, clientDeviceSystemVersion, clientDeviceModel, clientDeviceManufacturer, clientAppId, clientAppVersion)
} catch (e: ClientException) {
    println("4xx response calling UserApi#updateUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserApi#updateUser")
    e.printStackTrace()
}
```

### Parameters
| **authorization** | **kotlin.String**| Authorization token value | |
| **updateUserRequest** | [**UpdateUserRequest**](UpdateUserRequest.md)| Updated user object | |
| **clientDeviceId** | **kotlin.String**| Identifier of client device | [optional] |
| **clientDeviceSystemName** | **kotlin.String**| Name of client device&#39;s operating system | [optional] |
| **clientDeviceSystemVersion** | **kotlin.String**| Version of client device&#39;s operating system | [optional] |
| **clientDeviceModel** | **kotlin.String**| Name of client&#39;s device model defined by the manufacturer | [optional] |
| **clientDeviceManufacturer** | **kotlin.String**| Name of the manufacturer of client device | [optional] |
| **clientAppId** | **kotlin.String**| Identifier of client application | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **clientAppVersion** | **kotlin.String**| Version of client application | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

