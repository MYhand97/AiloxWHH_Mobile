package com.example.myapplication.data.api.response.penyimpanan

import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayCheckLocation

class ResponseDataCheckLocation {
    var message : String? = null
    var data : List<ResponsePutawayCheckLocation>? = null
    constructor(message: String?, data: List<ResponsePutawayCheckLocation>?){
        this.message = message
        this.data = data
    }
}