package com.example.ktortest.network

import android.util.Log
import com.example.ktortest.network.NetworkObject.HOST
import com.example.ktortest.network.NetworkObject.PATH
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkObject {
    //const val BASE_URL = "https://openapi.naver.com/v1/search/news.json"
    const val HOST = "api.github.com"
    const val PATH = "/users/Pluu"

    const val HEADER_ID = "X-Naver-Client-Id"
    const val HEADER_SECRET = "X-Naver-Client-Secret"
}

val ktorClient = HttpClient(CIO) {
    expectSuccess = true          //응답 유효성 검사 true/false
    //followRedirects  = false    //리다이렉트 true/false

    //Http 요청에 대해 default value를 세팅하는 역할
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = HOST 	//URL Host
            path(PATH)		//요청 EndPoint
        }

        //요청 Header 추가 (localProperies 네이버 API id, key)
        headers {
//            header(HEADER_ID, BuildConfig.NAVER_CLIENT_ID)
//            header(HEADER_SECRET, BuildConfig.NAVER_CLIENT_SECRET)
        }
    }

    // install: client configuration block에 plugin을 가져옴
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

    //Http 요청 로깅들을 만드는 로깅 역할
    install(Logging) {
        //logger = Logger.DEFAULT
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Ktor Logger", message)
            }
        }
        level = LogLevel.ALL
        //filter {  request -> request.url.host.contains("ktor.io") }
    }

    //응답의 상태를 기록하는 Observer
    install(ResponseObserver) {
        onResponse { response -> Log.d("HTTP status: ", "${response.status.value}") }
    }
}