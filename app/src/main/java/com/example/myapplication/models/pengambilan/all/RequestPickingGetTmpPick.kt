package com.example.myapplication.models.pengambilan.all

class RequestPickingGetTmpPick {
    var db_name : String? = null
    var task : String? = null
    var wave_plan_id : String? = null
    var loc_id : String? = null
    var username : String? = null
    constructor(db_name: String?, task: String?,
                wave_plan_id: String?, loc_id: String?, username: String?){
        this.db_name = db_name
        this.task = task
        this.wave_plan_id = wave_plan_id
        this.loc_id = loc_id
        this.username = username
    }
}