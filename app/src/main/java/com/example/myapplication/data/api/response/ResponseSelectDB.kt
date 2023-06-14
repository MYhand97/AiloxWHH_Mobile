package com.example.myapplication.data.api.response

import com.example.myapplication.models.ModelsSelectDB

class ResponseSelectDB {
    var message: String? = null
    var data: List<ModelsSelectDB>? = null
     constructor(message: String?, data: List<ModelsSelectDB>?){
        this.message = message
        this.data = data
    }
}