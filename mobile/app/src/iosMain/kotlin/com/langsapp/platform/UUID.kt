package com.langsapp.platform

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
