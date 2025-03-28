package com.example.ktortest.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class TestApi(private val client: HttpClient){
    suspend fun getNews(query: String) : HttpResponse = client.get{
        parameter("query", query)
    }

    suspend fun getTest(): HttpResponse = client.get {

    }

}