package com.example.myapplication.models.pengambilan.all.models

class ModelsWQMessage {
    var wq_message : String? = null
    var message : String? = null
    var confirm : String? = null
    constructor(wq_message: String?, message: String?, confirm: String?){
        this.wq_message = wq_message
        this.message = message
        this.confirm = confirm
    }
}