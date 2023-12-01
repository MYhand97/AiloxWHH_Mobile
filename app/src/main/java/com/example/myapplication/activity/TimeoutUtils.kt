package com.example.myapplication.activity

object TimeoutUtils {
    fun checkTimeout(lastTimeUser: Long): Boolean{
        val currentTime = System.currentTimeMillis()
        val timeoutThresholds: Long = 3 * 60 * 1000 // 3 Minutes in miliseconds

        return currentTime - lastTimeUser > timeoutThresholds
    }
}