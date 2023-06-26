package com.example.myapplication.activity.pengeluaran

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.activity.picking.pickingall.PickingScanBarcodeActivity
import com.example.myapplication.adapter.pengeluaran.AdapterDispModInValLpn
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModInValLpn
import com.example.myapplication.models.pengeluaran.pack.RequestDispModInValLpn
import com.example.myapplication.models.pengeluaran.pack.RequestDispScanValLpn
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispMessage
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispMessageWave
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModInValLpn
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatchModInValLpnActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var adapterDispModInValLpn : AdapterDispModInValLpn? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressDialog : Dialog? = null
    private var searchView : SearchView? = null
    private var data : List<ModelsDispModInValLpn>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_mod_in_val_lpn)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        valueScan = findViewById(R.id.ed_scan_pack)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title : TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("cust_name", null)+
                "\n"+session.getString("ship_number", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
        }

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, DispatchScanValLpnActivity::class.java)
                )
            }else{
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val btnScan : Button = findViewById(R.id.btn_scan)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        initProgressDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
        checkScanValue()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@DispatchModInValLpnActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val totalRow : TextView = findViewById(R.id.total_baris)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDispModInValLpn> = res.dispatchPackModInValLpn(
            RequestDispModInValLpn(
                db_name = session.getString("db_name", null),
                task = "mod_in_val_lpn",
                ship_header_id = session.getString("ship_header_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseDispModInValLpn>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDispModInValLpn>,
                response: Response<ResponseDispModInValLpn>
            ) {
                data = response.body()?.data
                val total_baris = data!!.size
                totalRow.text = "Jumlah Koli: $total_baris"
                adapterDispModInValLpn = AdapterDispModInValLpn(
                    applicationContext, data!!, object : AdapterDispModInValLpn.OnAdapterListener{
                        override fun onClick(list: ModelsDispModInValLpn) {
                            progressDialog!!.show()
                            session.edit()
                                .putString("wave_plans_id", list.wave_plans_id.toString())
                                .putString("lpn_id", list.pack_lpn_id.toString())
                                .apply()
                            progressDialog!!.dismiss()
                            startActivity(
                                Intent(applicationContext, DispatchModItemListActivity::class.java)
                            )
                        }
                    })
                adapterDispModInValLpn!!.setItemList(data as MutableList<ModelsDispModInValLpn>)
                recyclerView?.adapter = adapterDispModInValLpn
                adapterDispModInValLpn!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponseDispModInValLpn>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val edScan : TextInputEditText = findViewById(R.id.ed_scan_pack)
        val btnLanjut : Button = findViewById(R.id.btn_next)
        btnLanjut.setOnClickListener {
            progressDialog!!.show()
            if(edScan.text.toString() == ""){
                progressDialog!!.dismiss()
                edScan.error = "Label Pack tidak boleh kosong"
            }else{
                val res : RequestApi = ApiServer().koneksiRetrofit().create(
                    RequestApi::class.java
                )
                val row : Call<ModelsDispMessageWave> = res.dispatchPackScanValLpn(
                    RequestDispScanValLpn(
                        db_name = session.getString("db_name", null),
                        task = "check_mod_in_val_lpn",
                        ship_header_id = session.getString("ship_header_id", null),
                        lpn_id = edScan.text.toString()
                    )
                )
                row.enqueue(object : Callback<ModelsDispMessageWave>{
                    override fun onResponse(
                        call: Call<ModelsDispMessageWave>,
                        response: Response<ModelsDispMessageWave>
                    ) {
                        val message : String = response.body()?.message.toString()
                        val wave_plans_id : String = response.body()?.wave_plans_id.toString()

                        if(message == ""){
                            progressDialog!!.dismiss()
                            session.edit()
                                .putString("wave_plans_id", wave_plans_id)
                                .putString("lpn_id", edScan.text.toString())
                                .apply()
                            startActivity(
                                Intent(applicationContext, DispatchModItemListActivity::class.java)
                            )
                        }else{
                            progressDialog!!.dismiss()
                            edScan.error = message
                        }
                    }
                    override fun onFailure(call: Call<ModelsDispMessageWave>, t: Throwable) {
                        progressDialog!!.dismiss()
                        Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun swipeRefresh(){
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun search(searchView : SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterDispModInValLpn?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("nomor_resi", null)
            .apply()
    }

    private fun backBtnPressed(){
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, DispatchModInValResiActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreferences()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, DispatchModInValResiActivity::class.java)
        )
        finish()
    }

    companion object {
        var valueScan : TextInputEditText? = null
    }
}