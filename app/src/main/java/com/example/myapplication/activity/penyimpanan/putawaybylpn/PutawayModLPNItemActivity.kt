package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.penyimpanan.AdapterPutawayItemList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetItemInLPN
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetItemInLPN
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetItemInLPN
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutawayModLPNItemActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManagemet : LinearLayoutManager? = null
    private var list : List<ResponsePutawayGetItemInLPN>? = null
    private var adapterPutawayItemList : AdapterPutawayItemList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressBar : ProgressBar? = null
    private var searchView : SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_lpnitem)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener{ search(searchView!!) }

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun swipeRefresh(){
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManagemet = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManagemet
    }

    private fun retrieveData(){
        progressBar?.visibility = View.VISIBLE
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val tTotalQty : TextView = findViewById(R.id.total_qty)
        val res: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row: Call<ResponseDataPutawayGetItemInLPN> = res.putawayGetItemInLPN(
            RequestPutawayGetItemInLPN(
                db_name = session.getString("db_name", null),
                task = "get_item_in_lpn",
                lpn_id = session.getString("lpn_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseDataPutawayGetItemInLPN> {
            override fun onResponse(
                call: Call<ResponseDataPutawayGetItemInLPN>,
                response: Response<ResponseDataPutawayGetItemInLPN>
            ) {
                list = response.body()?.data
                tTotalQty.text = list!![0].total_qty
                adapterPutawayItemList = AdapterPutawayItemList(
                    applicationContext, list!!
                )
                adapterPutawayItemList!!.setListItem(list as MutableList<ResponsePutawayGetItemInLPN>)
                recyclerView?.adapter = adapterPutawayItemList
                adapterPutawayItemList!!.notifyDataSetChanged()
                progressBar?.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ResponseDataPutawayGetItemInLPN>, t: Throwable) {
                //TODO("Not yet implemented")
            }

        })
    }

    private fun search(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapterPutawayItemList?.filter?.filter(p0)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
    }

    @Deprecated("Deprecated in Java")
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
                Intent(applicationContext, PutawayModLocatingActivity::class.java)
            )
            finish()
        }
    }

}