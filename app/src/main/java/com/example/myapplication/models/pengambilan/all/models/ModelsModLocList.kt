package com.example.myapplication.models.pengambilan.all.models

class ModelsModLocList {
    var wq_id : String? = null
    var from_location: String? = null
    var loc_name : String? = null
    var qty : String? = null
    var qty_ambil : String? = null
    var qty_kurang : String? = null
    var jml_baris : String? = null
    constructor(
        wq_id: String?, from_location: String?,
        loc_name: String?, qty: String?,
        qty_ambil: String?, qty_kurang: String?,
        jml_baris: String?
    ){
        this.wq_id = wq_id
        this.from_location = from_location
        this.loc_name = loc_name
        this.qty = qty
        this.qty_ambil = qty_ambil
        this.qty_kurang = qty_kurang
        this.jml_baris = jml_baris
    }
}