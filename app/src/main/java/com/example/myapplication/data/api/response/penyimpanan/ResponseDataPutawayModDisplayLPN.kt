package com.example.myapplication.data.api.response.penyimpanan

import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayModDisplayLPN

class ResponseDataPutawayModDisplayLPN {
    var message : String? = null
    var rcpt_number : String? = null
    var rcpt_header_ids : String? = null
    var data : List<ResponsePutawayModDisplayLPN>? = null
    var total_qty : String? = null
    constructor(message: String?, rcpt_number: String?, rcpt_header_ids: String?,
    data: List<ResponsePutawayModDisplayLPN>?, total_qty: String?){
        this.message = message
        this.rcpt_number = rcpt_number
        this.rcpt_header_ids = rcpt_header_ids
        this.data = data
        this.total_qty = total_qty
    }
}