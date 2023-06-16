package com.example.myapplication.models.pengambilan.all

class RequestPickingModWaveList {
    var db_name : String? = null
    var task : String? = null
    var item_group_id : String? = null
    constructor(db_name: String?, task: String?, item_group_id: String?){
        this.db_name = db_name
        this.task = task
        this.item_group_id = item_group_id
    }
}