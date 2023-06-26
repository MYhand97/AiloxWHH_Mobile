package com.example.myapplication.models.pengeluaran.pack

class RequestDispModItemList {
    var db_name : String? = null
    var task : String? = null
    var wave_plans_id : String? = null
    var lpn_id : String? = null
    constructor(
        db_name: String?, task: String?,
        wave_plans_id: String?, lpn_id: String?
    ){
        this.db_name = db_name
        this.task = task
        this.wave_plans_id = wave_plans_id
        this.lpn_id = lpn_id
    }
}