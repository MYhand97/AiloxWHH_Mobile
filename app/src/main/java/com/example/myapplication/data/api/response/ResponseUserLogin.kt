package com.example.myapplication.data.api.response

import com.example.myapplication.models.ResponseModelsUsers

class ResponseUserLogin {
    var message: String? = null
    var data: List<ResponseModelsUsers>? = null
    constructor(message: String?, data: List<ResponseModelsUsers>?){
        this.message = message
        this.data = data
    }
}