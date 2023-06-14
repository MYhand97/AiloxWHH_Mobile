package com.example.myapplication.models

class RequestModelsUsers {
    var db_name: String? = null
    var username: String? = null
    var password: String? = null

    constructor(db_name: String?, username: String?, password: String?){
        this.db_name = db_name
        this.username = username
        this.password = password
    }
}