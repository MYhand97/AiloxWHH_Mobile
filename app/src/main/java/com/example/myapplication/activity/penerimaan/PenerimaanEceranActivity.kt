package com.example.myapplication.activity.penerimaan

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.activity.HomeActivity
import com.example.myapplication.activity.SubMenuActivity
import com.example.myapplication.activity.penerimaan.penerimaaneceran.EceranInValLPNActivity
import com.example.myapplication.adapter.penerimaan.AdapterPenerimaanEceran
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penerimaan.ResponseGetPenerimaanEceran
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.models.penerimaan.RequestPenerimaanEceran
import com.example.myapplication.models.penerimaan.ResponsePenerimaanEceran
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestSelectRcpt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenerimaanEceranActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponsePenerimaanEceran>? = null
    private var adapterPenerimaanEceran: AdapterPenerimaanEceran? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var searchView: SearchView? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_penerimaan_eceran)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
    }

    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val penerimaanProduksiTitleTV: TextView = findViewById(R.id.submenu_title)
        penerimaanProduksiTitleTV.text = session.getString("sub_menu_title", null)
        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        initCustomDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@PenerimaanEceranActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val resData: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetPenerimaanEceran> = resData.getPenerimaanEceran(
            RequestPenerimaanEceran(
                db_name = session.getString("db_name", null)
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetPenerimaanEceran>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseGetPenerimaanEceran>,
                response: Response<ResponseGetPenerimaanEceran>
            ) {
                list = response.body()?.data
                adapterPenerimaanEceran = AdapterPenerimaanEceran(
                    applicationContext, list!!, object : AdapterPenerimaanEceran.OnAdapterListener{
                        override fun onClick(list: ResponsePenerimaanEceran) {
                            customDialog!!.show()
                            val rcptHeader: Call<ResponseRcptHeader> = resData.selectReceipt(
                                RequestSelectRcpt(
                                    db_name = session.getString("db_name", null),
                                    task = "select_receipt",
                                    rcpt_header_id = list.rcpt_header_id
                                )
                            )
                            rcptHeader.enqueue(object : Callback<ResponseRcptHeader>{
                                override fun onResponse(
                                    call: Call<ResponseRcptHeader>,
                                    response: Response<ResponseRcptHeader>
                                ) {
                                    val message: String = response.body()?.message.toString()
                                    if(message == "Sukses"){
                                        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                            .edit()
                                            .putString("rcpt_header_id", list.rcpt_header_id)
                                            .putString("rcpt_number", list.rcpt_number)
                                            .apply()
                                        customDialog!!.dismiss()
                                        startActivity(
                                            Intent(applicationContext, EceranInValLPNActivity::class.java)
                                        )
                                    }else{
                                        customDialog!!.dismiss()
                                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                                    }
                                    //finish()
                                }

                                override fun onFailure(
                                    call: Call<ResponseRcptHeader>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
                                }
                            })
                            //Toast.makeText(applicationContext, list.rcpt_header_id+" - "+list.rcpt_number, Toast.LENGTH_LONG).show()
                        }
                    }
                )
                adapterPenerimaanEceran!!.setListItems(list as MutableList<ResponsePenerimaanEceran>)
                recyclerView?.adapter = adapterPenerimaanEceran
                adapterPenerimaanEceran!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseGetPenerimaanEceran>, t: Throwable) {
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapterPenerimaanEceran?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun swipeRefresh(){
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeSharedPreferences()
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("sub_menu_id", null)
            .putString("sub_menu_title", null)
            .putString("sub_menu_action", null)
            .apply()
    }

    private fun backBtnPressed(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener{
            customDialog!!.show()
            removeSharedPreferences()
            if(checkPageAccess.equals("true")){
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, HomeActivity::class.java)
                )
            }else{
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, SubMenuActivity::class.java)
                )
            }
            finish()
        }
    }

    override fun onBackPressed() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        customDialog!!.show()
        removeSharedPreferences()
        if(checkPageAccess.equals("true")){
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
            )
        }else{
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, SubMenuActivity::class.java)
            )
        }
        finish()
    }
}