package com.example.myapplication.data.api.response.penerimaan

import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseValItem

class ResponseEceranValItem {
    var message: String? = null
    var data: List<ResponseValItem>? = null
    constructor(message: String?, data: List<ResponseValItem>){
        this.message = message
        this.data = data
    }
}