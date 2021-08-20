package io.github.cclassen.oakdsl.builder

import com.fasterxml.jackson.databind.JsonNode
import io.github.cclassen.oakdsl.TestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OpenApiBuilderTest: TestBase() {

    @Test
    fun testInfo() {
        val yaml = build {
            info {
                version = "1.0.0"
            }
        }
        assertThat(yaml["info"]["version"].textValue()).isEqualTo("1.0.0")
    }

    @Test
    fun testServer() {
        val yaml = build {
            server("http://example.org", "Production server")
            server("http://localhost:8080", "Test server")
        }
        assertThat(yaml["servers"][0]["url"].textValue()).isEqualTo("http://example.org")
        assertThat(yaml["servers"][0]["description"].textValue()).isEqualTo("Production server")
        assertThat(yaml["servers"][1]["url"].textValue()).isEqualTo("http://localhost:8080")
        assertThat(yaml["servers"][1]["description"].textValue()).isEqualTo("Test server")
    }

    private fun checkMethod(yaml: JsonNode, method: String) {
        val endpoint = extractEndpoint(yaml, method, "/test")
        assertThat(endpoint["operationId"].textValue()).isEqualTo("testOperation")
        val response = extractDefaultResponse(endpoint)
        assertThat(response["description"].textValue()).isEqualTo("Success")
    }

    private fun checkMethodWithResponse(yaml: JsonNode, method: String) {
        val endpoint = extractEndpoint(yaml, method, "/test")
        assertThat(endpoint["operationId"].textValue()).isEqualTo("testOperation")
        val response = extractDefaultResponse(endpoint)
        assertThat(response["description"].textValue()).isEqualTo("Success")
        val schema = extractResponseSchema(response)
        assertThat(schema["\$ref"].textValue()).isEqualTo("#/components/schemas/" + TestObj::class.simpleName)
    }

    private fun checkMethodWithContentType(yaml: JsonNode, method: String) {
        val endpoint = extractEndpoint(yaml, method, "/test")
        assertThat(endpoint["operationId"].textValue()).isEqualTo("testOperation")
        val response = extractDefaultResponse(endpoint)
        assertThat(response["description"].textValue()).isEqualTo("Success")
        val schema = extractResponseSchema(response, "multipart/form-data")
        assertThat(schema["\$ref"].textValue()).isEqualTo("#/components/schemas/" + TestObj::class.simpleName)
    }

    @Test
    fun testPost() {
        val yaml = build {
            post("testOperation") / "test" def {}
        }
        checkMethod(yaml, "post")
    }

    @Test
    fun testPostWithResponse() {
        val yaml = build {
            post<TestObj>("testOperation") / "test" def {}
        }
        checkMethodWithResponse(yaml, "post")
    }

    @Test
    fun testPostWithContentType() {
        val yaml = build {
            post<TestObj>("testOperation", contentType = "multipart/form-data") / "test" def {}
        }
        checkMethodWithContentType(yaml, "post")
    }

    @Test
    fun testGet() {
        val yaml = build {
            get("testOperation") / "test" def {}
        }
        checkMethod(yaml, "get")
    }

    @Test
    fun testGetWithResponse() {
        val yaml = build {
            get<TestObj>("testOperation") / "test" def {}
        }
        checkMethodWithResponse(yaml, "get")
    }

    @Test
    fun testGetWithContentType() {
        val yaml = build {
            get<TestObj>("testOperation", contentType = "multipart/form-data") / "test" def {}
        }
        checkMethodWithContentType(yaml, "get")
    }

    @Test
    fun testPut() {
        val yaml = build {
            put("testOperation") / "test" def {}
        }
        checkMethod(yaml, "put")
    }

    @Test
    fun testPutWithResponse() {
        val yaml = build {
            put<TestObj>("testOperation") / "test" def {}
        }
        checkMethodWithResponse(yaml, "put")
    }

    @Test
    fun testPutWithContentType() {
        val yaml = build {
            put<TestObj>("testOperation", contentType = "multipart/form-data") / "test" def {}
        }
        checkMethodWithContentType(yaml, "put")
    }

    @Test
    fun testPatch() {
        val yaml = build {
            patch("testOperation") / "test" def {}
        }
        checkMethod(yaml, "patch")
    }

    @Test
    fun testPatchWithResponse() {
        val yaml = build {
            patch<TestObj>("testOperation") / "test" def {}
        }
        checkMethodWithResponse(yaml, "patch")
    }

    @Test
    fun testPatchWithContentType() {
        val yaml = build {
            patch<TestObj>("testOperation", contentType = "multipart/form-data") / "test" def {}
        }
        checkMethodWithContentType(yaml, "patch")
    }

    @Test
    fun testDelete() {
        val yaml = build {
            delete("testOperation") / "test" def {}
        }
        checkMethod(yaml, "delete")
    }

    @Test
    fun testDeleteWithResponse() {
        val yaml = build {
            delete<TestObj>("testOperation") / "test" def {}
        }
        checkMethodWithResponse(yaml, "delete")
    }

    @Test
    fun testDeleteWithContentType() {
        val yaml = build {
            delete<TestObj>("testOperation", contentType = "multipart/form-data") / "test" def {}
        }
        checkMethodWithContentType(yaml, "delete")
    }
}