package com.example.myapplication.models.penyimpanan.putawaybylpn

class RequestPutawayCheckLocation {
    var db_name : String? = null
    var task : String? = null
    var loc_cd : String? = null
    constructor(db_name: String?, task: String?, loc_cd: String?){
        this.db_name = db_name
        this.task = task
        this.loc_cd = loc_cd
    }
}