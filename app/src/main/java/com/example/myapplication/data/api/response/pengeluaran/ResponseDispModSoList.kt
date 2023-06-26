package com.example.myapplication.data.api.response.pengeluaran

import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModSoList

class ResponseDispModSoList {
    var message : String? = null
    var data : List<ModelsDispModSoList>? = null
    constructor(message: String?, data: List<ModelsDispModSoList>?){
        this.message = message
        this.data = data
    }
}