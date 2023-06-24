package com.example.myapplication.models.pengambilan.all

class RequestPickingPostToTmpPick {
    var db_name : String? = null
    var task : String? = null
    var item_barcode : String? = null
    var loc_id : String? = null
    var wave_plan_id : String? = null
    var username : String? = null
    constructor(db_name: String?, task: String?, item_barcode: String?,
        loc_id: String?, wave_plan_id: String?, username: String?){
        this.db_name = db_name
        this.task = task
        this.item_barcode = item_barcode
        this.loc_id = loc_id
        this.wave_plan_id = wave_plan_id
        this.username = username
    }
}