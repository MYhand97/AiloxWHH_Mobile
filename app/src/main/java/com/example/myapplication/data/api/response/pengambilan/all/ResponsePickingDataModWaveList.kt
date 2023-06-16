package com.example.myapplication.data.api.response.pengambilan.all

import com.example.myapplication.models.pengambilan.all.models.ModelsItemGroup
import com.example.myapplication.models.pengambilan.all.models.ModelsWaveList

class ResponsePickingDataModWaveList {
    var message : String? = null
    var item_group : List<ModelsItemGroup>? = null
    var wave_list : List<ModelsWaveList>? = null
    constructor(message: String?, item_group: List<ModelsItemGroup>?,
                wave_list: List<ModelsWaveList>?){
        this.message = message
        this.item_group = item_group
        this.wave_list = wave_list
    }
}