package com.example.myapplication.models.penyimpanan.putawaybylpn

class ResponsePutawayModDisplayLPN {
    var item_id : String? = null
    var item_description : String? = null
    var item_size : String? = null
    var item_number : String? = null
    var qty : String? = null
    constructor(item_id : String?, item_description: String?, item_size: String?,
    item_number: String?, qty: String?){
        this.item_id = item_id
        this.item_description = item_description
        this.item_size = item_size
        this.item_number = item_number
        this.qty = qty
    }
}