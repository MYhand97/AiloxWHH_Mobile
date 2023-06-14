package com.example.myapplication.models.penerimaan.penerimaaneceran

class RequestTmpToReceipt {
    var db_name : String? = null
    var task : String? = null
    var lpn_id : String? = null
    var rcpt_header_id : String? = null
    var rcpt_number : String? = null
    var username : String? = null
    constructor(db_name: String?, task: String?, lpn_id: String?,
                rcpt_header_id: String?, rcpt_number: String?, username: String?){
        this.db_name = db_name
        this.task = task
        this.lpn_id = lpn_id
        this.rcpt_header_id = rcpt_header_id
        this.rcpt_number = rcpt_number
        this.username = username
    }
}