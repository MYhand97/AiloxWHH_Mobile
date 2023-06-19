package com.example.myapplication.models.pengambilan.all.models

class ModelsItemGroup {
    var item_group_id : String? = null
    var item_group_name : String? = null
    var outbound_pref_id : String? = null
    constructor(item_group_id: String?, item_group_name: String?, outbound_pref_id: String?){
        this.item_group_id = item_group_id
        this.item_group_name = item_group_name
        this.outbound_pref_id = outbound_pref_id
    }
}