package com.example.myapplication.data.api.response.penyimpanan

import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetItemInLPN

class ResponseDataPutawayGetItemInLPN {
    var message : String? = null
    var data : List<ResponsePutawayGetItemInLPN>? = null
    constructor(message: String?, data: List<ResponsePutawayGetItemInLPN>){
        this.message = message
        this.data = data
    }
}