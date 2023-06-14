package com.example.myapplication.data.api.response

import com.example.myapplication.models.ResponseMainMenu

class ResponseGetMainMenu {
    var message: String? = null
    var data: List<ResponseMainMenu>? = null
    constructor(message: String?, data: List<ResponseMainMenu>?){
        this.message = message
        this.data = data
    }
}