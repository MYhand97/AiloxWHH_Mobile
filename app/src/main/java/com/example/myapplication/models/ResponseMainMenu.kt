package com.example.myapplication.models

class ResponseMainMenu {
    var rows: Int? = null
    var sub_menu_title: String? = null
    var master_sub_menu_id: String? = null
    constructor(rows: Int?, sub_menu_title: String?, master_sub_menu_id: String?){
        this.rows = rows
        this.sub_menu_title = sub_menu_title
        this.master_sub_menu_id = master_sub_menu_id
    }
}