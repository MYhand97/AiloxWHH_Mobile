package com.example.myapplication.models.pengambilan.all

class RequestPickingUpdateTmpPick {
    var db_name : String? = null
    var task : String? = null
    var wave_plan_id : String? = null
    var username : String? = null
    var from_location : String? = null
    constructor(db_name: String?, task: String?,
    wave_plan_id: String?, username: String?, from_location: String?){
        this.db_name = db_name
        this.task = task
        this.wave_plan_id = wave_plan_id
        this.username = username
        this.from_location = from_location
    }
}