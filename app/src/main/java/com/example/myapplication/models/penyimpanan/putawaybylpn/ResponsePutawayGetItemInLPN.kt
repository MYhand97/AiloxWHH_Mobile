package com.example.myapplication.models.penyimpanan.putawaybylpn

class ResponsePutawayGetItemInLPN {
    var item_id : String? = null
    var item_number : String? = null
    var item_description : String? = null
    var item_size : String? = null
    var qty : String? = null
    var total_qty : String? = null
    constructor(item_id: String?, item_number: String?, item_description: String?,
    item_size: String?, qty: String?, total_qty: String?){
        this.item_id = item_id
        this.item_number = item_number
        this.item_description = item_description
        this.item_size = item_size
        this.qty = qty
        this.total_qty = total_qty
    }
}