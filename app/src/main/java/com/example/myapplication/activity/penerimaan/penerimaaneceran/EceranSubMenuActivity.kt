package com.example.myapplication.activity.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.penerimaan.penerimaaneceran.AdapterEceranMenu
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseGetSubMenu
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.models.RequestSubMenu
import com.example.myapplication.models.ResponseSubMenu
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestReleaseRcpt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EceranSubMenuActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponseSubMenu>? = null
    private var adapterEceranMenu: AdapterEceranMenu? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_eceran_sub_menu)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu: TextView = findViewById(R.id.submenu_title)
        titleMenu.text = "Penerimaan\n"+
                session.getString("rcpt_number", null)+
                "\nLPN: "+
                session.getString("lpn_id", null)

        initCustomDialog()

        setupRecycleView()
        retrieveSubMenu()
        backBtnPress()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@EceranSubMenuActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun setupRecycleView(){
        recyclerView = findViewById(R.id.my_recycleview_submenu)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveSubMenu(){
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseGetSubMenu> = res.getEceranSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name", null),
                master_sub_menu_id = session.getString("sub_menu_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseGetSubMenu>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                list = response.body()?.data
                adapterEceranMenu = AdapterEceranMenu(applicationContext, list!!, object : AdapterEceranMenu.OnAdapterListener{
                    override fun onClick(list: ResponseSubMenu) {
                        when (list.sub_menu_title) {
                            "Pre-Pack" -> {
                                session.edit().putString("pp_mode", "Single").apply()
                            }
                            "Multi Pre-Pack" -> {
                                session.edit().putString("pp_mode", "Multi").apply()
                            }
                            "Solid" -> {
                                session.edit().putString("pp_mode", "Solid").apply()
                            }
                        }
                        startActivity(
                            Intent(applicationContext, EceranInValItemAcitivity::class.java)
                        )
                        //finish()
                    }
                })
                recyclerView?.adapter = adapterEceranMenu
                adapterEceranMenu!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {
                customDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseRcptStatus()
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("lpn_id", null)
            .putString("rcpt_header_id", null)
            .putString("rcpt_number", null)
            .apply()
    }

    private fun releaseRcptStatus(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row: Call<ResponseRcptHeader> = res.releaseReceipt(
            RequestReleaseRcpt(
                db_name = session.getString("db_name", null),
                task = "release_receipt",
                rcpt_header_id = session.getString("rcpt_header_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseRcptHeader>{
            override fun onResponse(
                call: Call<ResponseRcptHeader>,
                response: Response<ResponseRcptHeader>
            ) {
                /*removeSharedPreferences()
                startActivity(
                    Intent(applicationContext, PenerimaanEceranActivity::class.java)
                )*/
                //finish()
            }

            override fun onFailure(call: Call<ResponseRcptHeader>, t: Throwable) {
                //Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("lpn_id", null)
            .apply()
    }

    private fun backBtnPress(){
        val btnBack: ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener{
            customDialog!!.show()
            removeSharedPreferences()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, EceranInValLPNActivity::class.java)
            )
            //finish()
        }
    }

    override fun onBackPressed() {
        customDialog!!.show()
        removeSharedPreferences()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, EceranInValLPNActivity::class.java)
        )
        //finish()
    }
}