package com.example.myapplication.activity.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.activity.penerimaan.PenerimaanEceranActivity
import com.example.myapplication.adapter.penerimaan.penerimaaneceran.AdapterEceranScanBarcode
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penerimaan.ResponseEceranGetTmpReceive
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestGetTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestReleaseRcpt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestTmpToReceipt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestUpdateTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseGetTmpReceive
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EceranInValScanBarcodeAcitivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponseGetTmpReceive>? = null
    private var adapterEceranScanBarcode: AdapterEceranScanBarcode? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var searchView: SearchView? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_eceran_in_val_scan_barcode)

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
                Intent(applicationContext, EceranInValItemAcitivity::class.java)
            )
            //finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        customDialog!!.show()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, EceranInValItemAcitivity::class.java)
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

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startActivity(
                    Intent(applicationContext, EceranScanBarcodeActivity::class.java)
                )
            } else {
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val btnScan : Button = findViewById(R.id.btn_scan_barcode)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        val btnSimpan : Button = findViewById(R.id.btn_penerimaan_simpan)
        btnSimpan.setOnClickListener {
            updateTmpReceive()
        }

        initCustomDialog()

        backBtnPressed()
        setupRecycleView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@EceranInValScanBarcodeAcitivity)
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
        val row : Call<ResponseEceranGetTmpReceive> = res.eceranGetTmpReceive(
            RequestGetTmpReceive(
                db_name = session.getString("db_name", null),
                task = "get_tmp_receive",
                rcpt_header_id = session.getString("rcpt_header_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ResponseEceranGetTmpReceive>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseEceranGetTmpReceive>,
                response: Response<ResponseEceranGetTmpReceive>
            ) {
                list = response.body()?.data

                adapterEceranScanBarcode = AdapterEceranScanBarcode(
                    applicationContext, list!!, object : AdapterEceranScanBarcode.OnAdapterListener{
                        override fun onClick(list: ResponseGetTmpReceive) {
                            customDialog!!.show()
                            var qty : Int = list.t_scan_qty!!.toInt() - list.kurang_qty!!.toInt()
                            if(qty < 0){
                                qty = 0
                            }
                            customDialog!!.dismiss()
                            Toast.makeText(applicationContext, list.item_id+" - "+list.item_barcode+"\nLebih Scan : "+qty, Toast.LENGTH_LONG).show()
                        }
                    }
                )

                adapterEceranScanBarcode!!.setListItem(list as MutableList<ResponseGetTmpReceive>)
                recyclerView?.adapter = adapterEceranScanBarcode
                adapterEceranScanBarcode!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseEceranGetTmpReceive>, t: Throwable) {
                customDialog!!.dismiss()
                //Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterEceranScanBarcode?.filter?.filter(newText)
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
            .putString("pp_mode", null)
            .apply()
    }

    private fun updateTmpReceive(){
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row: Call<ResponseRcptHeader> = res.updateTmpReceive(
            RequestUpdateTmpReceive(
                db_name = session.getString("db_name", null),
                task = "update_tmp_receive",
                rcpt_header_id = session.getString("rcpt_header_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ResponseRcptHeader>{
            override fun onResponse(
                call: Call<ResponseRcptHeader>,
                response: Response<ResponseRcptHeader>
            ) {
                val message: String? = response.body()?.message
                if(message == "Updated"){
                    tmpToReceipt()
                }
            }

            override fun onFailure(call: Call<ResponseRcptHeader>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun tmpToReceipt(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseRcptHeader> = res.tmpToReceipt(
            RequestTmpToReceipt(
                db_name = session.getString("db_name", null),
                task = "tmp_to_receipt",
                lpn_id = session.getString("lpn_id", null),
                rcpt_header_id = session.getString("rcpt_header_id", null),
                rcpt_number = session.getString("rcpt_number", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ResponseRcptHeader>{
            override fun onResponse(
                call: Call<ResponseRcptHeader>,
                response: Response<ResponseRcptHeader>
            ) {
                val message: String? = response.body()?.message
                if(message == "received"){
                    Toast.makeText(applicationContext, "Barang sudah diterima semua", Toast.LENGTH_SHORT).show()
                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                        .edit()
                        .putString("lpn_id", null)
                        .putString("rcpt_header_id", null)
                        .putString("rcpt_number", null)
                        .putString("pp_mode", null)
                        .apply()
                    customDialog!!.dismiss()
                    startActivity(
                        Intent(applicationContext, PenerimaanEceranActivity::class.java)
                    )
                }else{
                    Toast.makeText(applicationContext, message.toString(), Toast.LENGTH_SHORT).show()
                    customDialog!!.dismiss()
                    startActivity(
                        Intent(applicationContext, EceranInValItemAcitivity::class.java)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseRcptHeader>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
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
}