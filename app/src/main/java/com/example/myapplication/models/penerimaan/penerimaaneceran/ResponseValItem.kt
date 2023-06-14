package com.example.myapplication.models.penerimaan.penerimaaneceran

class ResponseValItem {
    var t_rcpt_ordered_qty : String? = null
    var t_rcpt_received_qty : String? = null
    var kurang_qty : String? = null
    var item_id : String? = null
    var item_description : String? = null
    var uom : String? = null
    var item_group_id : String? = null
    var item_barcode : String? = null
    var item_design : String? = null
    var item_size : String? = null
    var pp_mode : String? = null
    constructor(t_rcpt_ordered_qty: String?, t_rcpt_received_qty: String?, kurang_qty: String?,
    item_id: String?, item_description: String?, uom: String?, item_group_id: String?,
    item_barcode: String?, item_design: String?, item_size: String?, pp_mode: String?){
        this.t_rcpt_ordered_qty = t_rcpt_ordered_qty
        this.t_rcpt_received_qty = t_rcpt_received_qty
        this.kurang_qty = kurang_qty
        this.item_id = item_id
        this.item_description = item_description
        this.uom = uom
        this.item_group_id = item_group_id
        this.item_barcode = item_barcode
        this.item_design = item_design
        this.item_size = item_size
        this.pp_mode = pp_mode
    }
}