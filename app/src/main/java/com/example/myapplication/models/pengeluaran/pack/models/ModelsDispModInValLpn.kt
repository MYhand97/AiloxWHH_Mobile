package com.example.myapplication.models.pengeluaran.pack.models

class ModelsDispModInValLpn {
    var wave_plans_id : String? = null
    var pack_lpn_id : String? = null
    var opd_pack_qty : String? = null
    var uom : String? = null
    var panjang_roll : String? = null
    constructor(
        wave_plans_id: String?,
        pack_lpn_id: String?, opd_pack_qty: String?,
        uom: String?, panjang_roll: String?
    ){
        this.wave_plans_id = wave_plans_id
        this.pack_lpn_id = pack_lpn_id
        this.opd_pack_qty = opd_pack_qty
        this.uom = uom
        this.panjang_roll = panjang_roll
    }
}