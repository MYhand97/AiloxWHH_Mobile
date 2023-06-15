package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.example.myapplication.adapter.penyimpanan.AdapterPutawayMasterLocation
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetMasterLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetMasterLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetMasterLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutawayModLocatingManualActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponsePutawayGetMasterLocation>? = null
    private var adapterPutawayMasterLocation: AdapterPutawayMasterLocation? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_locating_manual)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title: TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)
        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressBar?.visibility = View.VISIBLE
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDataPutawayGetMasterLocation> = res.putawayGetMasterLocation(
            RequestPutawayGetMasterLocation(
                db_name = session.getString("db_name", null),
                task = "get_master_location"
            )
        )
        row.enqueue(object : Callback<ResponseDataPutawayGetMasterLocation>{
            override fun onResponse(
                call: Call<ResponseDataPutawayGetMasterLocation>,
                response: Response<ResponseDataPutawayGetMasterLocation>
            ) {
                list = response.body()?.data
                adapterPutawayMasterLocation = AdapterPutawayMasterLocation(
                    applicationContext, list!!, object : AdapterPutawayMasterLocation.onAdapterListener{
                        override fun onClick(list: ResponsePutawayGetMasterLocation) {
                            session.edit()
                                .putString("loc_name_baru", list.loc_name)
                                .putString("loc_cd_baru", list.loc_cd)
                                .putString("locating_manual", "true")
                                .apply()
                            startActivity(
                                Intent(applicationContext, PutawayModInLocActivity::class.java)
                            )
                        }
                    }
                )
                adapterPutawayMasterLocation!!.setListItem(list as MutableList<ResponsePutawayGetMasterLocation>)
                recyclerView?.adapter = adapterPutawayMasterLocation
                adapterPutawayMasterLocation!!.notifyDataSetChanged()
                progressBar?.visibility = View.GONE
            }
            override fun onFailure(call: Call<ResponseDataPutawayGetMasterLocation>, t: Throwable) {
                TODO("Not yet implemented")
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

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterPutawayMasterLocation?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun backBtnPressed(){
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            startActivity(
                Intent(applicationContext, PutawayModLocatingActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(applicationContext, PutawayModLocatingActivity::class.java)
        )
        finish()
    }
}