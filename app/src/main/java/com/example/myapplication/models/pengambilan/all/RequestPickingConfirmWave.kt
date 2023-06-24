package com.example.myapplication.models.pengambilan.all

class RequestPickingConfirmWave {
    var db_name : String? = null
    var task : String? = null
    var wave_plan_id : String? = null
    var outbound_pref_id : String? = null
    constructor(db_name: String?, task: String?,
    wave_plan_id: String?, outbound_pref_id: String?){
        this.db_name = db_name
        this.task = task
        this.wave_plan_id = wave_plan_id
        this.outbound_pref_id = outbound_pref_id
    }
}