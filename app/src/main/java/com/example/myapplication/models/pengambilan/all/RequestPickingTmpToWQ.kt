package com.example.myapplication.models.pengambilan.all

class RequestPickingTmpToWQ {
    var db_name : String? = null
    var task : String? = null
    var wave_plan_id : String? = null
    var lpn_pack : String? = null
    var pack_type : String? = null
    var username : String? = null
    constructor(
        db_name: String?, task: String?,
        wave_plan_id: String?, lpn_pack: String?,
        pack_type: String?, username: String?
    ){
        this.db_name = db_name
        this.task = task
        this.wave_plan_id = wave_plan_id
        this.lpn_pack = lpn_pack
        this.pack_type = pack_type
        this.username = username
    }
}