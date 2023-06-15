package com.example.myapplication.models.penyimpanan.putawaybylpn

class RequestSaveItem {
    var db_name : String? = null
    var task : String? = null
    var username : String? = null
    var lpn_id : String? = null
    var rcpt_number : String? = null
    var rcpt_header_ids : String? = null
    var loc_name : String? = null
    var loc_id : String? = null
    constructor(
        db_name: String?, task: String?,
        username: String?, lpn_id: String?,
        rcpt_number: String?, rcpt_header_ids: String?,
        loc_name: String?, loc_id: String?
    ){
        this.db_name = db_name
        this.task = task
        this.username = username
        this.lpn_id = lpn_id
        this.rcpt_number = rcpt_number
        this.rcpt_header_ids = rcpt_header_ids
        this.loc_name = loc_name
        this.loc_id = loc_id
    }
}