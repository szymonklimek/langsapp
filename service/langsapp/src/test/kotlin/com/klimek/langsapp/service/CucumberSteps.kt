package com.klimek.langsapp.service

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class CucumberSteps {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    private var authenticatedUserId: String? = null
    private var requestMethod: HttpMethod? = null
    private var requestPath: String? = null
    private var requestBody: String? = null
    private var actionResult: FluxExchangeResult<String>? = null

    @Given("authenticated user with id {string}")
    fun authenticatedUserWithId(id: String) {
        authenticatedUserId = id
    }

    @Given("request method {string}")
    fun requestMethod(method: String) {
        requestMethod = HttpMethod.valueOf(method)
    }

    @Given("request path {string}")
    fun requestPath(path: String) {
        requestPath = path
    }

    @Given("request body")
    fun requestBody(body: String) {
        requestBody = body
    }

    @When("request is sent")
    fun `request is sent`() {
        println("Sending request $requestMethod, $requestPath, $requestBody")
        actionResult = webTestClient
            .method(requestMethod!!)
            .uri(requestPath!!)
            .contentType(MediaType.APPLICATION_JSON)
            .apply {
                if (authenticatedUserId != null) {
                    header("Authorization", authenticatedUserId)
                }
            }
            .body(BodyInserters.fromValue(requestBody!!))
            .exchange()
            .returnResult<String>()
    }

    @Then("response code is {string}")
    fun responseCodeIs(status: String) {
        assertEquals(status, actionResult!!.status.value().toString())
    }

    @Then("response body is")
    fun responseBodyIs(body: String) {
        assertEquals(body, String(actionResult!!.responseBodyContent!!))
    }
}
