package com.example.ktortest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ktortest.network.TestRepository
import com.example.ktortest.ui.theme.KtorTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                        // CoroutineScope를 기억하여 UI 이벤트에 따라 코루틴을 실행할 수 있음
                        val coroutineScope = rememberCoroutineScope()
                        val testRepository = TestRepository()

                        Button(onClick = {
                            Log.d("TTTT", "button click")
                            println("버튼 클릭됨!1111")

                            coroutineScope.launch {
                                println("버튼 클릭됨!2222")
                                testRepository.getTest().collect {
                                    println("${it.name}")
                                }
                            }
                        }) {
                            Text(text = "테스트 버튼")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KtorTestTheme {
        Greeting("Android")
    }
}