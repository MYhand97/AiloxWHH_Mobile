package com.example.myapplication.models.penerimaan.penerimaaneceran

class RequestReleaseRcpt {
    var db_name: String? = null
    var task: String? = null
    var rcpt_header_id: String? = null
    constructor(db_name: String?, task: String?, rcpt_header_id: String?){
        this.db_name = db_name
        this.task = task
        this.rcpt_header_id = rcpt_header_id
    }
}