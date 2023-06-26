package com.example.myapplication.data.api.response.pengeluaran

import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModInValLpn

class ResponseDispModInValLpn {
    var message : String? = null
    var data : List<ModelsDispModInValLpn>? = null
    constructor(message: String?, data: List<ModelsDispModInValLpn>?){
        this.message = message
        this.data = data
    }
}