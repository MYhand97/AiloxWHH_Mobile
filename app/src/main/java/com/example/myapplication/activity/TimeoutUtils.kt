package com.example.myapplication.activity

object TimeoutUtils {
    fun checkTimeout(lastTimeUser: Long): Boolean{
        val currentTime = System.currentTimeMillis()
        val timeoutThresholds: Long = 1 * 60 * 1000 // 1 Minutes in miliseconds

        return currentTime - lastTimeUser > timeoutThresholds
    }
}