package com.example.myapplication.data.api.response.pengeluaran

import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModWaveList

class ResponseDispModWaveList {
    var message : String? = null
    var data : List<ModelsDispModWaveList>? = null
    constructor(message: String?, data: List<ModelsDispModWaveList>?){
        this.message = message
        this.data = data
    }
}