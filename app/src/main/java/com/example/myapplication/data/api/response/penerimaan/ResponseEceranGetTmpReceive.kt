package com.example.myapplication.data.api.response.penerimaan

import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseGetTmpReceive

class ResponseEceranGetTmpReceive {
    var message: String? = null
    var data: List<ResponseGetTmpReceive>? = null
    constructor(message: String?, data: List<ResponseGetTmpReceive>?){
        this.message = message
        this.data = data
    }
}