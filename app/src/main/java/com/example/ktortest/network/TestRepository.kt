package com.example.ktortest.network

import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRepository{
    private val testApi = TestApi(ktorClient)

    fun getTest(): Flow<User> = flow{
        try {
            testApi.getTest().run {
                if (status.isSuccess()){
                    //Data Reponse 자동 역직렬화
                    val response = body<User>()

                    emit(response)
                }else{
                    //Api Error 처리
                }
            }
        }catch (e: Exception){
            //Network Error 처리
        }
    }
}