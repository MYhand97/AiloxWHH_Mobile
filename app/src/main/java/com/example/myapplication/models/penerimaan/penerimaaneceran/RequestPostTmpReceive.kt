package com.example.myapplication.models.penerimaan.penerimaaneceran

class RequestPostTmpReceive {
    var db_name : String? = null
    var task : String? = null
    var item_barcode : String? = null
    var rcpt_header_id : String? = null
    var username : String? = null
    constructor(db_name: String?, task: String?, item_barcode: String?,
    rcpt_header_id: String?, username: String?){
        this.db_name = db_name
        this.task = task
        this.item_barcode = item_barcode
        this.rcpt_header_id = rcpt_header_id
        this.username = username
    }
}