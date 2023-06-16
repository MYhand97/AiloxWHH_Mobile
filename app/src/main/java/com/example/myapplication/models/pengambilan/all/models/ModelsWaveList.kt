package com.example.myapplication.models.pengambilan.all.models

class ModelsWaveList {
    var wave_plan_id : String? = null
    var wave_comment : String? = null
    var no_so : String? = null
    var cust_name : String? = null
    constructor(wave_plan_id: String?, wave_comment: String?, no_so: String?, cust_name: String?){
        this.wave_plan_id = wave_plan_id
        this.wave_comment = wave_comment
        this.no_so = no_so
        this.cust_name = cust_name
    }
}