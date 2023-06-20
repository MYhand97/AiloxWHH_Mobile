package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsLocItemList

class ResponsePickingDataModLocItemList {
    var message : String? = null
    var data : List<ModelsLocItemList>? = null
    constructor(message: String?, data: List<ModelsLocItemList>?){
        this.message = message
        this. data = data
    }
}