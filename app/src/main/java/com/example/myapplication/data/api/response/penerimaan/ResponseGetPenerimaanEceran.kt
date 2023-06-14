package com.example.myapplication.data.api.response.penerimaan

import com.example.myapplication.models.penerimaan.ResponsePenerimaanEceran

class ResponseGetPenerimaanEceran {
    var message: String? = null
    var data: List<ResponsePenerimaanEceran>? = null
    constructor(message: String?, data: List<ResponsePenerimaanEceran>?){
        this.message = message
        this.data = data
    }
}