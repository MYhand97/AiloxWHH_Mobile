package com.example.myapplication.models.penyimpanan.putawaybylpn

class ResponsePutawayGetMasterLocation {
    var loc_name : String? = null
    var loc_cd : String? = null
    constructor(loc_name: String?, loc_cd: String?){
        this.loc_name = loc_name
        this.loc_cd = loc_cd
    }
}