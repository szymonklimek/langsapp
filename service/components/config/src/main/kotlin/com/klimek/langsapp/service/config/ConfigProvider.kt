package com.klimek.langsapp.service.config

interface ConfigProvider {
    fun getValue(key: String): String
}
