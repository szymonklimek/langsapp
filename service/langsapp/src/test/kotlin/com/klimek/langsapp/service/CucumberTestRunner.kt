package com.klimek.langsapp.service

import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasspathResource("integration-tests")
class CucumberTestRunner
