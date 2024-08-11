package com.langsapp.data

import com.langsapp.model.LanguageSetting
import com.langsapp.model.content.ContentUnit

class ContentDatabase {
    private var units: List<ContentUnit> = mutableListOf()

    fun getAllUnits(languageSetting: LanguageSetting) = units

    fun insertUnits(languageSetting: LanguageSetting, units: List<ContentUnit>) {
        this.units = units
    }
}
