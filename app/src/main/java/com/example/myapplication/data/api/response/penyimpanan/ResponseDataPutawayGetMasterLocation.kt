package com.example.myapplication.data.api.response.penyimpanan

import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetMasterLocation

class ResponseDataPutawayGetMasterLocation {
    var message : String? = null
    var data : List<ResponsePutawayGetMasterLocation>? = null
    constructor(message: String?, data: List<ResponsePutawayGetMasterLocation>?){
        this.message = message
        this.data = data
    }
}