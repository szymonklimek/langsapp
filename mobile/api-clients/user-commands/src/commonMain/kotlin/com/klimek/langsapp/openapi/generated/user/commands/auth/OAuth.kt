package com.klimek.langsapp.openapi.generated.user.commands.auth

class OAuth : Authentication {
    var accessToken: String? = null

    override fun apply(query: MutableMap<String, List<String>>, headers: MutableMap<String, String>) {
        val token: String = accessToken ?: return
        headers["Authorization"] = "Bearer $token"
    }
}
