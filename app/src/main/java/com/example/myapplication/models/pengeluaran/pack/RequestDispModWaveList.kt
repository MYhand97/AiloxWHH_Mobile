package com.example.myapplication.models.pengeluaran.pack

class RequestDispModWaveList {
    var db_name : String? = null
    var task : String? = null
    constructor(db_name: String?, task: String?){
        this.db_name = db_name
        this.task = task
    }
}