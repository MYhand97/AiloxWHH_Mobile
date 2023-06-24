package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsTmpPick

class ResponsePickingGetTmpPick {
    var message : String? = null
    var data : List<ModelsTmpPick>? = null
    constructor(message: String, data: List<ModelsTmpPick>?){
        this.message = message
        this.data = data
    }
}