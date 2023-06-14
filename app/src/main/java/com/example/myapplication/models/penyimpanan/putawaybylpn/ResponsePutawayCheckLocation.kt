package com.example.myapplication.models.penyimpanan.putawaybylpn

class ResponsePutawayCheckLocation {
    var vLocId : String? = null
    var vLocName : String? = null
    var vAwal : String? = null
    var vLocTypeId : String? = null
    constructor(vLocId: String?, vLocName: String?, vAwal: String?, vLocTypeId: String?){
        this.vLocId = vLocId
        this.vLocName = vLocName
        this.vAwal = vAwal
        this.vLocTypeId = vLocTypeId
    }
}