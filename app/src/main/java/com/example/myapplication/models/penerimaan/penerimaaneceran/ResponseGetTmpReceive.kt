package com.example.myapplication.models.penerimaan.penerimaaneceran

class ResponseGetTmpReceive {
    var item_barcode : String? = null
    var item_id : String? = null
    var t_scan_qty : String? = null
    var t_rcpt_ordered_qty : String? = null
    var kurang_qty : String? = null
    var t_received_qty : String? = null
    var t_receive_qty : String? = null
    var t_diff_qty : String? = null
    var disp_detail_id : String? = null
    var rcpt_header_id : String? = null
    var username : String? = null
    constructor(item_barcode: String?, item_id: String?, t_scan_qty: String?,
                t_rcpt_ordered_qty: String?, kurang_qty: String?, t_received_qty: String?,
                t_receive_qty: String?, t_diff_qty: String?, disp_detail_id: String?,
                rcpt_header_id: String?, username: String?){
        this.item_barcode = item_barcode
        this.item_id = item_id
        this.t_scan_qty = t_scan_qty
        this.t_rcpt_ordered_qty = t_rcpt_ordered_qty
        this.kurang_qty = kurang_qty
        this.t_received_qty = t_received_qty
        this.t_receive_qty = t_receive_qty
        this.t_diff_qty = t_diff_qty
        this.disp_detail_id = disp_detail_id
        this.rcpt_header_id = rcpt_header_id
        this.username = username
    }
}