package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsModLocList

class ResponsePickingDataModLocList {
    var message : String? = null
    var data : List<ModelsModLocList>? = null
    constructor(message: String?, data: List<ModelsModLocList>?){
        this.message = message
        this.data = data
    }
}