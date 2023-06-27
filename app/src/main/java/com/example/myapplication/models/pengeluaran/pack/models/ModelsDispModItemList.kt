package com.example.myapplication.models.pengeluaran.pack.models

class ModelsDispModItemList {
    var pack_detail_id : String? = null
    var disp_detail_id : String? = null
    var item_id : String? = null
    var item_number : String? = null
    var pack_qty : String? = null
    var uom : String? = null
    constructor(
        pack_detail_id: String?, disp_detail_id: String?,
        item_id: String?, item_number: String,
        pack_qty: String?, uom: String?
    ){
        this.pack_detail_id = pack_detail_id
        this.disp_detail_id = disp_detail_id
        this.item_id = item_id
        this.item_number = item_number
        this.pack_qty = pack_qty
        this.uom = uom
    }
}