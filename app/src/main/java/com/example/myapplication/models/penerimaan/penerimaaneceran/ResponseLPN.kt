package com.example.myapplication.models.penerimaan.penerimaaneceran

class ResponseLPN {
    var receipt_lpn_id: String? = null
    var lpn_id: String? = null
    var error_flag: String? = null
    var mode_lpn: String? = null
    var rcpt_mudo: String? = null
    constructor(receipt_lpn_id: String?, lpn_id: String?, error_flag: String?,
    mode_lpn: String?, rcpt_mudo: String?){
        this.receipt_lpn_id = receipt_lpn_id
        this.lpn_id = lpn_id
        this.error_flag = error_flag
        this.mode_lpn = mode_lpn
        this.rcpt_mudo = rcpt_mudo
    }
}