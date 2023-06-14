package com.example.myapplication.models.penerimaan

class ResponsePenerimaanEceran {
    var rows: Int? = null
    var rcpt_header_id: String? = null
    var rcpt_number: String? = null
    var supplier_id: String? = null
    constructor(rows: Int?, rcpt_header_id: String?, rcpt_number: String?, supplier_id: String?){
        this.rows = rows
        this.rcpt_header_id = rcpt_header_id
        this.rcpt_number = rcpt_number
        this.supplier_id = supplier_id
    }
}