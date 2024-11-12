package com.langsapp.platform

data class AppInstallationInfo(
    val installationId: String,
    val appId: String,
    val appVersion: String,
    val deviceModel: String,
    val deviceManufacturer: String,
    val deviceSystemName: String,
    val deviceSystemVersion: String,
)
