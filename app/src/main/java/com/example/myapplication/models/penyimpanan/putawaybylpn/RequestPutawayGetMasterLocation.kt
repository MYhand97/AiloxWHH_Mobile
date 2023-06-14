package com.example.myapplication.models.penyimpanan.putawaybylpn

class RequestPutawayGetMasterLocation {
    var db_name : String? = null
    var task : String? = null
    constructor(db_name: String?, task: String?){
        this.db_name = db_name
        this.task = task
    }
}