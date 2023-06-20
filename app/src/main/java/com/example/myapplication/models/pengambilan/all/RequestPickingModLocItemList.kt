package com.example.myapplication.models.pengambilan.all

class RequestPickingModLocItemList {
    var db_name : String? = null
    var task : String? = null
    var outbound_pref_id : String? = null
    var loc_id : String? = null
    var wave_plan_id : String? = null
    constructor(
        db_name: String?, task: String?,
        outbound_pref_id: String?, loc_id: String?,
        wave_plan_id: String?
    ){
        this.db_name = db_name
        this.task = task
        this.outbound_pref_id = outbound_pref_id
        this.loc_id = loc_id
        this.wave_plan_id = wave_plan_id
    }
}