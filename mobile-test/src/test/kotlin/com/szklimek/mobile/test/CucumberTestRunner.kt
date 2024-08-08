package com.szklimek.mobile.test

import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasspathResource("mobile")
class CucumberTestRunner
