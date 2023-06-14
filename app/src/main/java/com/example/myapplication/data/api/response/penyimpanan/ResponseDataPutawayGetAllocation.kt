package com.example.myapplication.data.api.response.penyimpanan

import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetAllocation

class ResponseDataPutawayGetAllocation {
    var message : String? = null
    var data : ResponsePutawayGetAllocation? = null
    constructor(message: String?, data: ResponsePutawayGetAllocation){
        this.message = message
        this.data = data
    }
}