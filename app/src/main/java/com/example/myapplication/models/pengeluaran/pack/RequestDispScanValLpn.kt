package com.example.myapplication.models.pengeluaran.pack

class RequestDispScanValLpn {
    var db_name : String? = null
    var task : String? = null
    var ship_header_id : String? = null
    var lpn_id : String? = null
    constructor(
        db_name: String?, task: String?,
        ship_header_id: String?,lpn_id: String?
    ){
        this.db_name = db_name
        this.task = task
        this.ship_header_id = ship_header_id
        this.lpn_id = lpn_id
    }
}