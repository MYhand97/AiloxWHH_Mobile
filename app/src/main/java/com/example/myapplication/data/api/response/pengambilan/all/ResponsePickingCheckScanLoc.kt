package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsLocation

class ResponsePickingCheckScanLoc {
    var message : String? = null
    var data : List<ModelsLocation>? = null
    constructor(message: String?){
        this.message = message
        this.data = data
    }
}