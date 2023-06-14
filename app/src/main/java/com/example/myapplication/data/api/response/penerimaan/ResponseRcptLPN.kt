package com.example.myapplication.data.api.response.penerimaan

import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseLPN

class ResponseRcptLPN {
    var message: String? = null
    var data: List<ResponseLPN>? = null
    constructor(message: String?, data: List<ResponseLPN>?){
        this.message = message
        this.data = data
    }
}