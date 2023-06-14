package com.example.myapplication.data.api.response

import com.example.myapplication.models.ResponseSubMenu

class ResponseGetSubMenu {
    var message: String? = null
    var data: List<ResponseSubMenu>? = null
    constructor(message: String?, data: List<ResponseSubMenu>?){
        this.message = message
        this.data = data
    }
}