package com.example.myapplication

import android.app.Application

class UserTimeout {
    companion object{
        var lastUserActivityTimestamp: Long = 0
    }
}