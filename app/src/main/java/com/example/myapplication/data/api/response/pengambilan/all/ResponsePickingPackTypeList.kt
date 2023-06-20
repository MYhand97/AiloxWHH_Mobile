package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsPackTypeList

class ResponsePickingPackTypeList {
    var pack_list : List<ModelsPackTypeList>? = null
    constructor(pack_list: List<ModelsPackTypeList>?){
        this.pack_list = pack_list
    }
}