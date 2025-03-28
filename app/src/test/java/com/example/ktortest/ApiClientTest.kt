package com.example.ktortest

import com.example.ktortest.network.TestApi
import com.example.ktortest.network.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test

class ApiClientTest {

    private fun createMockClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/users/Pluu" -> {
                    respond(
                        content = """{"login":"Pluu","id":1534926,"node_id":"MDQ6VXNlcjE1MzQ5MjY=","avatar_url":"https://avatars.githubusercontent.com/u/1534926?v=4","gravatar_id":"","url":"https://api.github.com/users/Pluu","html_url":"https://github.com/Pluu","followers_url":"https://api.github.com/users/Pluu/followers","following_url":"https://api.github.com/users/Pluu/following{/other_user}","gists_url":"https://api.github.com/users/Pluu/gists{/gist_id}","starred_url":"https://api.github.com/users/Pluu/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/Pluu/subscriptions","organizations_url":"https://api.github.com/users/Pluu/orgs","repos_url":"https://api.github.com/users/Pluu/repos","events_url":"https://api.github.com/users/Pluu/events{/privacy}","received_events_url":"https://api.github.com/users/Pluu/received_events","type":"User","user_view_type":"public","site_admin":false,"name":"pluulove22","company":"@kakaobank","blog":"http://pluu.github.io/","location":"Korea Suwon","email":null,"hireable":null,"bio":null,"twitter_username":null,"public_repos":117,"public_gists":12,"followers":476,"following":64,"created_at":"2012-03-14T00:34:27Z","updated_at":"2025-03-15T05:18:00Z"}""",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                    )
                }
                else -> {
                    respond(
                        content = "Not Found",
                        status = HttpStatusCode.NotFound
                    )
                }
            }
        }

        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true          // Json string을 읽기 편하게 만들어줌
                        isLenient = true            // 따옴표 규칙 완화
                        encodeDefaults = true       // null 또는 잘못된 값이 들어간 경우 default property value로 대체
                        ignoreUnknownKeys = true    // Field 값이 없는 경우 무시할지 (모델에 없고, json에 있는 경우 해당 key 무시)
                    }
                )

            }

        }
    }

    @Test
    fun `test getUser returns expected data`() = runBlocking {
        // MockEngine을 사용하여 클라이언트 생성
        val client = createMockClient()
        println("start")

        // 실제 getUserList() 호출
        val apiClient = TestApi(client)
        val users = apiClient.getTest()
        users.run {
            if (status.isSuccess()){
                //Data Reponse 자동 역직렬화
                val response = body<User>()
                println("response")
                println(response)
                assertEquals("pluulove", response.name)
                println(response.name)
            }else{
                println("failed")

                //Api Error 처리
            }
        }

        client.close()
    }
}