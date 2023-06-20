package com.example.myapplication.models.pengambilan.all

class RequestPickingCheckScanLoc {
    var db_name : String? = null
    var task : String? = null
    var loc_cd : String? = null
    var wave_plan_id : String? = null
    constructor(db_name: String?, task: String?, loc_name: String?, wave_plan_id: String?){
        this.db_name = db_name
        this.task = task
        this.loc_cd = loc_cd
        this.wave_plan_id = wave_plan_id
    }
}