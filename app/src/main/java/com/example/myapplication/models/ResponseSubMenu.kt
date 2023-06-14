package com.example.myapplication.models

class ResponseSubMenu {
    var rows: Int? = null
    var sub_menu_title: String? = null
    var sub_menu_id: String? = null
    var sub_menu_action: String? = null
    constructor(rows: Int?, sub_menu_title: String?, sub_menu_id: String?, sub_menu_action: String?){
        this.rows = rows
        this.sub_menu_title = sub_menu_title
        this.sub_menu_id = sub_menu_id
        this.sub_menu_action = sub_menu_action
    }
}