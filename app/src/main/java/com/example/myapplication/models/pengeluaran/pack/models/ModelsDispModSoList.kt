package com.example.myapplication.models.pengeluaran.pack.models

class ModelsDispModSoList {
    var ship_header_id : String? = null
    var ship_number : String? = null
    var ship_date : String? = null
    var company_id : String? = null
    var whouse_id : String? = null
    var ship_header_user_def1 : String? = null
    var ship_header_user_def2 : String? = null
    constructor(
        ship_header_id: String?, ship_number: String?,
        ship_date: String?, company_id: String?,
        whouse_id: String?, ship_header_user_def1: String?,
        ship_header_user_def2: String?
    ){
        this.ship_header_id = ship_header_id
        this.ship_number = ship_number
        this.ship_date = ship_date
        this.company_id = company_id
        this.whouse_id = whouse_id
        this.ship_header_user_def1 = ship_header_user_def1
        this.ship_header_user_def2 = ship_header_user_def2
    }
}