package com.example.myapplication.models.pengambilan.all.models

class ModelsTmpPick {
    var item_barcode : String? = null
    var item_id : String? = null
    var t_scan_qty : String? = null
    var t_picked_qty : String? = null
    var available_qty : String? = null
    var wq_id : String? = null
    var inv_id : String? = null
    var user_name : String? = null
    var err_msg : String? = null
    constructor(
        item_barcode: String?, item_id: String?,
        t_scan_qty: String?, t_picked_qty: String?,
        available_qty: String?,
        wq_id: String?, inv_id: String?,
        user_name: String?, err_msg: String?
    ){
        this.item_barcode = item_barcode
        this.item_id = item_id
        this.t_scan_qty = t_scan_qty
        this.t_picked_qty = t_picked_qty
        this.available_qty = available_qty
        this.wq_id = wq_id
        this.inv_id = inv_id
        this.user_name = user_name
        this.err_msg = err_msg
    }
}