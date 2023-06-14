package com.example.myapplication.models.penerimaan.penerimaaneceran

class RequestReleaseLpn {
    var db_name: String? = null
    var task: String? = null
    var lpn_id: String? = null
    constructor(db_name: String?, task: String?, lpn_id: String?){
        this.db_name = db_name
        this.task = task
        this.lpn_id = lpn_id
    }
}