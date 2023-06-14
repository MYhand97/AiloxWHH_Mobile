package com.example.myapplication.models.penyimpanan.putawaybylpn

class ResponsePutawayGetAllocation {
    var loc_name : String? = null
    var loc_cd : String? = null
    var err_message : String? = null
    var ship_number : String? = null
    constructor(loc_name: String?, loc_cd: String?, err_message: String?, ship_number: String?){
        this.loc_name = loc_name
        this.loc_cd = loc_cd
        this.err_message = err_message
        this.ship_number = ship_number
    }
}