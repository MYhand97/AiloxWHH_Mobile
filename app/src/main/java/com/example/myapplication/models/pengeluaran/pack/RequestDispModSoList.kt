package com.example.myapplication.models.pengeluaran.pack

class RequestDispModSoList {
    var db_name : String? = null
    var task : String? = null
    var cust_id : String? = null
    constructor(db_name: String?, task: String?, cust_id: String?){
        this.db_name = db_name
        this.task = task
        this.cust_id = cust_id
    }
}