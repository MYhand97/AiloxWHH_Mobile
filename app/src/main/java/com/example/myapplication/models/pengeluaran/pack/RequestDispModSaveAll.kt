package com.example.myapplication.models.pengeluaran.pack

class RequestDispModSaveAll {
    var db_name : String? = null
    var task : String? = null
    var ship_header_id : String? = null
    var wave_plans_id : String? = null
    var dn : String? = null
    var resi : String? = null
    var lpn_id : String? = null
    var username : String? = null
    constructor(
        db_name: String?, task: String?,
        ship_header_id: String?,
        wave_plans_id: String?,
        dn: String?, resi: String?,
        lpn_id: String?, username: String?
    ){
        this.db_name = db_name
        this.task = task
        this.ship_header_id = ship_header_id
        this.wave_plans_id = wave_plans_id
        this.dn = dn
        this.resi = resi
        this.lpn_id = lpn_id
        this.username = username
    }
}