package com.example.myapplication.activity.penyimpanan.putawaybylpn

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
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_locating_manual)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        initCustomDialog()

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

    private fun initCustomDialog(){
        customDialog = Dialog(this@PutawayModLocatingManualActivity)
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
                            customDialog!!.show()
                            session.edit()
                                .putString("loc_name_baru", list.loc_name)
                                .putString("loc_cd_baru", list.loc_cd)
                                .putString("locating_manual", "true")
                                .apply()
                            customDialog!!.dismiss()
                            startActivity(
                                Intent(applicationContext, PutawayModInLocActivity::class.java)
                            )
                        }
                    }
                )
                adapterPutawayMasterLocation!!.setListItem(list as MutableList<ResponsePutawayGetMasterLocation>)
                recyclerView?.adapter = adapterPutawayMasterLocation
                adapterPutawayMasterLocation!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponseDataPutawayGetMasterLocation>, t: Throwable) {
                customDialog!!.dismiss()
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
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayModLocatingActivity::class.java)
            )
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialog!!.show()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PutawayModLocatingActivity::class.java)
        )
        finish()
    }
}