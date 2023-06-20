package com.example.myapplication.models.pengambilan.all.models

class ModelsLocItemList {
    var wq_id : String? = null
    var from_location : String? = null
    var loc_name : String? = null
    var item_id : String? = null
    var item_number : String? = null
    var roll_length : String? = null
    var lpn_id : String? = null
    var qty : String? = null
    var qty_ambil : String? = null
    var qty_kurang : String? = null
    var jml_baris : String? = null
    constructor(
        wq_id: String?, from_location: String?, loc_name: String?,
        item_id: String?, item_number: String?, roll_length: String?,
        lpn_id: String?, qty: String?, qty_ambil: String?,
        qty_kurang: String?, jml_baris: String?
    ){
        this.wq_id = wq_id
        this.from_location = from_location
        this.loc_name = loc_name
        this.item_id = item_id
        this.item_number = item_number
        this.roll_length = roll_length
        this.lpn_id = lpn_id
        this.qty = qty
        this.qty_ambil = qty_ambil
        this.qty_kurang = qty_kurang
        this.jml_baris = jml_baris
    }
}