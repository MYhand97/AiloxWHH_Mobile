package com.example.myapplication.models

class ResponseModelsUsers {
    var user_id: String? = null
    var user_fullname: String? = null
    var user_name: String? = null
    var user_group_id: String? = null
    var is_login: String? = null
    constructor(
        user_id: String?, user_fullname: String?, user_name: String?,
        user_group_id: String?, is_login: String?
    ){
        this.user_id = user_id
        this.user_fullname = user_fullname
        this.user_name = user_name
        this.user_group_id = user_group_id
        this.is_login = is_login
    }
}