package com.klimek.langsapp.service.config

import com.ecwid.consul.v1.ConsulClient

class ConsulConfigProvider(
    consulUrl: String,
    directory: String,
) : ConfigProvider {
    private val values = ConsulClient(consulUrl)
        .getKVValues("$directory/")
        .value
        .associate { it.key.replace("$directory/", "") to it.decodedValue }
        .apply { println("Loaded config keys: $keys") }

    override fun getValue(key: String): String = values[key] ?: ""
}
