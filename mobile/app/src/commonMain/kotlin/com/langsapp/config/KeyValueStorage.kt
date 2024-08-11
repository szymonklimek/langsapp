package com.langsapp.config

interface KeyValueStorage {
    fun get(key: String): String?

    fun getAll(): Map<String, String>

    fun set(key: String, value: String)

    fun remove(key: String)
}
