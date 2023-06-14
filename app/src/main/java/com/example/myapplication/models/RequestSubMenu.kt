package com.example.myapplication.models

class RequestSubMenu {
    var db_name: String? = null
    var master_sub_menu_id: String? = null
    constructor(db_name: String?, master_sub_menu_id: String?){
        this.db_name = db_name
        this.master_sub_menu_id = master_sub_menu_id
    }
}