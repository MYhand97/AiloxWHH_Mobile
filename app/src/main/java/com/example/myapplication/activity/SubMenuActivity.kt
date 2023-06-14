package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.penerimaan.PenerimaanEceranActivity
import com.example.myapplication.activity.penyimpanan.putawaybylpn.PutawayInValLpnActivity
import com.example.myapplication.adapter.AdapterGetSubMenu
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseGetSubMenu
import com.example.myapplication.models.RequestSubMenu
import com.example.myapplication.models.ResponseSubMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubMenuActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponseSubMenu>? = null
    private var adapterGetSubMenu: AdapterGetSubMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sub_menu)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        backBtnPress()
        setupRecycleView()
        retrieveSubMenu()
    }

    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val subMenuTitleTV: TextView = findViewById(R.id.submenu_title)
        subMenuTitleTV.text = session.getString("main_menu_title", null)

        backBtnPress()
        setupRecycleView()
        retrieveSubMenu()
    }

    private fun setupRecycleView(){
        recyclerView = findViewById(R.id.my_recycleview_submenu)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveSubMenu(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetSubMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetSubMenu> = retGetSubMenu.getSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name", null),
                master_sub_menu_id = session.getString("main_menu_id", null)
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetSubMenu> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                list = response.body()?.data
                adapterGetSubMenu = AdapterGetSubMenu(applicationContext, list!!, object : AdapterGetSubMenu.OnAdapterListener{
                    override fun onClick(list: ResponseSubMenu) {
                        when(list.sub_menu_action){
                            "PenerimaanEceranActivity" -> {
                                getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                    .edit()
                                    .putString("fromBotNav", "false")
                                    .putString("sub_menu_id", list.sub_menu_id)
                                    .putString("sub_menu_title", list.sub_menu_title)
                                    .putString("sub_menu_action", list.sub_menu_action)
                                    .apply()
                                startActivity(
                                    Intent(applicationContext, PenerimaanEceranActivity::class.java)
                                )
                            }
                            "PutawayInValLpnActivity" -> {
                                getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                    .edit()
                                    .putString("fromBotNav", "false")
                                    .putString("sub_menu_id", list.sub_menu_id)
                                    .putString("sub_menu_title", list.sub_menu_title)
                                    .putString("sub_menu_action", list.sub_menu_action)
                                    .apply()
                                startActivity(
                                    Intent(applicationContext, PutawayInValLpnActivity::class.java)
                                )
                            }
                            else -> {
                                Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                            }
                        }
                        /*if(list.sub_menu_action.equals("PenerimaanEceranActivity")){
                            getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                .edit()
                                .putString("fromBotNav", "false")
                                .putString("sub_menu_id", list.sub_menu_id)
                                .putString("sub_menu_title", list.sub_menu_title)
                                .putString("sub_menu_action", list.sub_menu_action)
                                .apply()
                            startActivity(
                                Intent(applicationContext, PenerimaanEceranActivity::class.java)
                            )
                            finish()
                        }else{
                            Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                        }*/
                    }
                })
                recyclerView?.adapter = adapterGetSubMenu
                adapterGetSubMenu!!.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("main_menu_id", null)
            .putString("main_menu_title", null)
            .apply()
    }

    private fun backBtnPress(){
        val btnBack: ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener{
            removeSharedPreferences()
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        removeSharedPreferences()
        startActivity(
            Intent(applicationContext, HomeActivity::class.java)
        )
        finish()
    }
}