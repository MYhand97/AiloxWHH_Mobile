package com.example.myapplication.models

class ModelsSelectDB {
    var task: String? = null
    var db_seq: String? = null
    var db_name: String? = null
    var db_title: String? = null

    constructor(task:String?, db_seq: String?, db_name: String?, db_title: String?){
        this.task = task
        this.db_seq = db_seq
        this.db_name = db_name
        this.db_title = db_title
    }
}