package com.example.ktortest.network

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String? = null
)