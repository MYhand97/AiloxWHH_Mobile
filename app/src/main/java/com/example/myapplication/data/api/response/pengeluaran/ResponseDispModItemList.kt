package com.example.myapplication.data.api.response.pengeluaran

import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModItemList

class ResponseDispModItemList {
    var message : String? = null
    var data : List<ModelsDispModItemList>? = null
    constructor(message: String?, data: List<ModelsDispModItemList>?){
        this.message = message
        this.data = data
    }
}