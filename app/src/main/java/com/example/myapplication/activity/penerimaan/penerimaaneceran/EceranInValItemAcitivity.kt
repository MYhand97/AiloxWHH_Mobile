package com.example.myapplication.activity.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.penerimaan.penerimaaneceran.AdapterEceranValItem
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penerimaan.ResponseEceranValItem
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestReleaseRcpt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestValItem
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseValItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EceranInValItemAcitivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponseValItem>? = null
    private var adapterEceranValItem: AdapterEceranValItem? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var searchView: SearchView? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_eceran_in_val_item)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
    }

    private fun backBtnPressed(){
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, EceranSubMenuActivity::class.java)
            )
            //finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        customDialog!!.show()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, EceranSubMenuActivity::class.java)
        )
        //finish()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu: TextView = findViewById(R.id.submenu_title)
        titleMenu.text = "Penerimaan\n"+
                session.getString("rcpt_number", null)+
                "\nLPN: "+
                session.getString("lpn_id", null)+" - "+session.getString("pp_mode", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        val btnScan : Button = findViewById(R.id.btn_scan)
        btnScan.setOnClickListener {
            startActivity(
                Intent(applicationContext, EceranInValScanBarcodeAcitivity::class.java)
            )
        }

        when(session.getString("pp_mode", null)){
            "Single" -> {
                val layoutButton : LinearLayout = findViewById(R.id.layoutButton)
                layoutButton.visibility = View.GONE
            }
            "Multi" -> {
                val layoutButton : LinearLayout = findViewById(R.id.layoutButton)
                layoutButton.visibility = View.GONE
            }
            else -> {
                val layoutButton : LinearLayout = findViewById(R.id.layoutButton)
                layoutButton.visibility = View.VISIBLE
            }
        }

        initCustomDialog()

        backBtnPressed()
        setupRecycleView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@EceranInValItemAcitivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun setupRecycleView(){
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
        val row : Call<ResponseEceranValItem> = res.eceranValItem(
            RequestValItem(
                db_name = session.getString("db_name", null),
                task = "get_receipt_detail",
                rcpt_header_id = session.getString("rcpt_header_id", null),
                pp_mode = session.getString("pp_mode", null)
            )
        )
        row.enqueue(object : Callback<ResponseEceranValItem>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseEceranValItem>,
                response: Response<ResponseEceranValItem>
            ) {
                list = response.body()?.data
                adapterEceranValItem = AdapterEceranValItem(
                    applicationContext, list!!, object : AdapterEceranValItem.OnAdapterListener{
                        override fun onClick(list: ResponseValItem) {

                            when(session.getString("pp_mode", null)){
                                "Single" -> {
                                    Toast.makeText(applicationContext, list.item_id+" - "+list.item_description, Toast.LENGTH_LONG).show()
                                }
                                "Multi" -> {
                                    Toast.makeText(applicationContext, list.item_id+" - "+list.item_description, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    //Toast.makeText(applicationContext, list.item_id+" - "+list.item_description, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                )

                adapterEceranValItem!!.setListItem(list as MutableList<ResponseValItem>)
                recyclerView?.adapter = adapterEceranValItem
                adapterEceranValItem!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseEceranValItem>, t: Throwable) {
                customDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapterEceranValItem?.filter?.filter(newText)
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
        releaseRcptStatus()
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("lpn_id", null)
            .putString("rcpt_header_id", null)
            .putString("rcpt_number", null)
            .apply()
    }

    private fun releaseRcptStatus(){
        customDialog!!.show()
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
                customDialog!!.dismiss()
                /*removeSharedPreferences()
                startActivity(
                    Intent(applicationContext, PenerimaanEceranActivity::class.java)
                )*/
                //finish()
            }

            override fun onFailure(call: Call<ResponseRcptHeader>, t: Throwable) {
                customDialog!!.dismiss()
                //Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }
        })
    }
}