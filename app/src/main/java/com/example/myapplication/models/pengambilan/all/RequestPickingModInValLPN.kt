package com.example.myapplication.models.pengambilan.all

class RequestPickingModInValLPN {
    var db_name : String? = null
    var task : String? = null
    var lpn_id : String? = null
    var wave_plan_id : String? = null
    constructor(db_name: String?, task: String?, lpn_id: String?, wave_plan_id: String?){
        this.db_name = db_name
        this.task = task
        this.lpn_id = lpn_id
        this.wave_plan_id = wave_plan_id
    }
}