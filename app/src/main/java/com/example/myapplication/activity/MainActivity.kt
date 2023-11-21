package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.AdapterSelectDB
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseSelectDB
import com.example.myapplication.models.ModelsSelectDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView?= null
    private var linearLayoutManager: LinearLayoutManager?= null
    private var list: List<ModelsSelectDB> ?= ArrayList()
    private var adapterSelectdb: AdapterSelectDB ?= null
    private var swipeRefreshLayout: SwipeRefreshLayout?= null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        retrieveSelectDB()
        swipeRefresh()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveSelectDB()
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.my_recycleview)
        swipeRefreshLayout = findViewById(R.id.swl_data)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun swipeRefresh(){
        swipeRefreshLayout?.setOnRefreshListener {
            retrieveSelectDB()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun retrieveSelectDB(){
        progressBar?.visibility = View.VISIBLE
        val dbData: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseSelectDB> = dbData.retrieveSelectDB(
            ModelsSelectDB(
                task="get_api",
                db_seq=null,
                db_name=null,
                db_title=null
            )
        )
        tampilData.enqueue(object : Callback<ResponseSelectDB>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ResponseSelectDB>, response: Response<ResponseSelectDB>) {
                list = response.body()?.data

                adapterSelectdb = AdapterSelectDB(applicationContext, list!!, object : AdapterSelectDB.OnAdapterListener{
                    override fun onClick(list: ModelsSelectDB) {
                        val postData: Call<ResponseSelectDB> = dbData.retrieveSelectDB(
                            ModelsSelectDB(
                                task="post_api",
                                db_seq=list.db_seq,
                                db_name=list.db_name,
                                db_title=list.db_title
                            )
                        )
                        postData.enqueue(object : Callback<ResponseSelectDB>{
                            override fun onResponse(call: Call<ResponseSelectDB>,response: Response<ResponseSelectDB>) {
                                // session
                                getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                    .edit()
                                    .putString("db_name", list.db_name)
                                    .putString("db_title", list.db_title)
                                    .apply()
                                startActivity(
                                    Intent(applicationContext, LoginActivity::class.java)
                                )
                                finish()
                            }

                            override fun onFailure(call: Call<ResponseSelectDB>, t: Throwable) {
                                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
                            }

                        })
                    }
                })
                recyclerView?.adapter = adapterSelectdb
                adapterSelectdb!!.notifyDataSetChanged()
                progressBar?.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseSelectDB>, t: Throwable) {
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }
        })
    }
}