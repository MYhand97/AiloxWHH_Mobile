package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsPackTypeList

class ResponsePickingDataModInValLPN {
    var message : String? = null
    var pack_list : List<ModelsPackTypeList>? = null
    constructor(message: String?, pack_list: List<ModelsPackTypeList>?){
        this.message = message
        this.pack_list = pack_list
    }
}