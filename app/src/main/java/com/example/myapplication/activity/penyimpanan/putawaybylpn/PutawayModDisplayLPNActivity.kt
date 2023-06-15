package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)+
                "\nLokasi: "+ session.getString("loc_name", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
        saveItem()
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
        progressBar?.visibility = View.VISIBLE
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
                progressBar?.visibility = View.INVISIBLE
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
                    loc_name = session.getString("loc_name", null),
                    loc_id = session.getString("loc_id", null)
                )
            )
            row.enqueue(object : Callback<ResponseSaveItem>{
                override fun onResponse(
                    call: Call<ResponseSaveItem>,
                    response: Response<ResponseSaveItem>
                ) {
                    val message = response.body()?.message.toString()
                    if(message.isEmpty()){
                        removeSharedPreferences()
                        startActivity(
                            Intent(applicationContext, PutawayInValLpnActivity::class.java)
                        )
                        finish()
                    }else{
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseSaveItem>, t: Throwable) {
                    TODO("Not yet implemented")
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
            .putString("err_message", null)
            .putString("ship_number", null)
            .putString("rcpt_number", null)
            .putString("rcpt_header_ids", null)
            .apply()
    }

    override fun onBackPressed() {
        startActivity(
            Intent(applicationContext, PutawayModLocatingActivity::class.java)
        )
        finish()
    }

    private fun backBtnPressed(){
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            startActivity(
                Intent(applicationContext, PutawayModDisplayLPNActivity::class.java)
            )
            finish()
        }
    }
}