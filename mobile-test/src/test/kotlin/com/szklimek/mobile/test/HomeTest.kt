package com.szklimek.mobile.test

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL


class HomeTest {

    @Test
    fun appStarts() {
        assertEquals(4, 2 + 2)
        val options = UiAutomator2Options()
            .setUdid("emulator-5554")
            .setAppPackage("com.langsapp.android")
            .setAppActivity("com.langsapp.android.MainActivity")
            .setAppWaitForLaunch(true)

//            .setApp("/home/myapp.apk")
//            .setApp("com.langsapp.android.LangsappApplication")
        val driver: AndroidDriver = AndroidDriver( // The default URL in Appium 1 is http://127.0.0.1:4723/wd/hub
            URL("http://127.0.0.1:4723"), options
        )
        try {
            val el = driver.findElement(AppiumBy.name("Skip"))
            el.click()
            driver.pageSource
        } finally {
            driver.quit()
        }
    }
}
