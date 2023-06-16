package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.penyimpanan.AdapterPutawayModDisplayLPN
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayModDisplayLPN
import com.example.myapplication.data.api.response.penyimpanan.ResponseSaveItem
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayModDisplayLPN
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestSaveItem
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayModDisplayLPN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutawayModDisplayLPNActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var list : List<ResponsePutawayModDisplayLPN>? = null
    private var adapterPutawayModDisplayLPN : AdapterPutawayModDisplayLPN? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressBar : ProgressBar? = null
    private var searchView : SearchView? = null
    private var loc_name : String? = null
    private var loc_cd : String? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_display_lpn)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
        saveItem()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

        //check loc_name dan loc_cd
        if(session.getString("loc_name_baru", null).equals(null) &&
            session.getString("loc_cd_baru", null).equals(null)){
            loc_name = session.getString("loc_name", null).toString()
            loc_cd = session.getString("loc_cd", null).toString()
        }else{
            loc_name = session.getString("loc_name_baru", null).toString()
            loc_cd = session.getString("loc_cd_baru", null).toString()
        }
        //end check

        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)+
                "\nLokasi: "+ loc_name

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        initCustomDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
        saveItem()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@PutawayModDisplayLPNActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun swipeRefresh() {
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapterPutawayModDisplayLPN?.filter?.filter(p0)
                return true
            }
        })
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveData() {
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val tTotalQty : TextView = findViewById(R.id.total_qty)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDataPutawayModDisplayLPN> = res.putawayModDisplayLPN(
            RequestPutawayModDisplayLPN(
                db_name = session.getString("db_name", null),
                task = "mod_display_lpn",
                lpn_id = session.getString("lpn_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseDataPutawayModDisplayLPN>{
            override fun onResponse(
                call: Call<ResponseDataPutawayModDisplayLPN>,
                response: Response<ResponseDataPutawayModDisplayLPN>
            ) {
                list = response.body()?.data
                tTotalQty.text = response.body()?.total_qty.toString()

                session.edit()
                    .putString("rcpt_header_ids", response.body()?.rcpt_header_ids.toString())
                    .putString("rcpt_number", response.body()?.rcpt_number.toString())
                    .apply()

                adapterPutawayModDisplayLPN = AdapterPutawayModDisplayLPN(
                    applicationContext, list!!
                )
                adapterPutawayModDisplayLPN!!.setListItem(list as MutableList<ResponsePutawayModDisplayLPN>)
                recyclerView?.adapter = adapterPutawayModDisplayLPN
                adapterPutawayModDisplayLPN!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseDataPutawayModDisplayLPN>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveItem(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

        val btnSimpan : Button = findViewById(R.id.btn_simpan)
        btnSimpan.setOnClickListener {
            customDialog!!.show()
            val res : RequestApi = ApiServer().koneksiRetrofit().create(
                RequestApi::class.java
            )
            val row : Call<ResponseSaveItem> = res.putawaySaveItem(
                RequestSaveItem(
                    db_name = session.getString("db_name", null),
                    task = "save_item",
                    username = session.getString("username", null),
                    lpn_id = session.getString("lpn_id", null),
                    rcpt_number = session.getString("rcpt_number", null),
                    rcpt_header_ids = session.getString("rcpt_header_ids", null),
                    loc_name = loc_name,
                    loc_id = session.getString("loc_id", null)
                )
            )
            row.enqueue(object : Callback<ResponseSaveItem>{
                override fun onResponse(
                    call: Call<ResponseSaveItem>,
                    response: Response<ResponseSaveItem>
                ) {
                    when(val message = response.body()?.message.toString()){
                        "Berhasil Simpan" -> {
                            removeSharedPreferences()
                            customDialog!!.dismiss()
                            startActivity(
                                Intent(applicationContext, PutawayInValLpnActivity::class.java)
                            )
                            finish()
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            customDialog!!.dismiss()
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseSaveItem>, t: Throwable) {
                    customDialog!!.dismiss()
                }

            })
        }
    }

    private fun removeSharedPreferences(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        session.edit()
            .putString("lpn_id", null)
            .putString("loc_name", null)
            .putString("loc_cd", null)
            .putString("loc_name_baru", null)
            .putString("loc_cd_baru", null)
            .putString("locating_manual", null)
            .putString("err_message", null)
            .putString("ship_number", null)
            .putString("rcpt_number", null)
            .putString("rcpt_header_ids", null)
            .apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialog!!.show()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PutawayModInLocActivity::class.java)
        )
        finish()
    }

    private fun backBtnPressed(){
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayModInLocActivity::class.java)
            )
            finish()
        }
    }
}