package com.langsapp

import com.langsapp.config.KeyValueStorage

class InMemoryKeyValueStorage : KeyValueStorage {
    private val map = mutableMapOf<String, String>()
    override fun get(key: String): String? = map[key]
    override fun set(key: String, value: String) = map.set(key, value)
    override fun remove(key: String) {
        map.remove(key)
    }
}
