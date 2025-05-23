package com.example.myapplication.activity.picking.pickingall

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.myapplication.adapter.pengambilan.all.AdapterPickingAllModLocList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingCheckScanLoc
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModLocList
import com.example.myapplication.models.pengambilan.all.RequestPickingCheckScanLoc
import com.example.myapplication.models.pengambilan.all.RequestPickingModLocList
import com.example.myapplication.models.pengambilan.all.models.ModelsLocation
import com.example.myapplication.models.pengambilan.all.models.ModelsModLocList
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModLocListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var data : List<ModelsModLocList>? = null
    private var location : List<ModelsLocation>? = null
    private var adapterPickingAllModLocList : AdapterPickingAllModLocList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var searchView : SearchView? = null
    private var progressDialog : Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_mod_loc_list)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        valueScan = findViewById(R.id.ed_scanLokasi)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("no_so", null)+
                "\n"+session.getString("pack_type_name", null)+": "+session.getString("lpn_id", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
        }

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, PickingScanLocListActivity::class.java)
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
        checkScanValue()
        retrieveData()
    }

    private fun initProgressDialog() {
        progressDialog = Dialog(this@PickingModLocListActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingDataModLocList> = res.pickingALlModLocList(
            RequestPickingModLocList(
                db_name = session.getString("db_name", null),
                task = "mod_loc_list",
                outbound_pref_id = session.getString("outbound_pref_id", null),
                wave_plan_id = session.getString("wave_plan_id", null)
            )
        )
        row.enqueue(object : Callback<ResponsePickingDataModLocList>{
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            override fun onResponse(
                call: Call<ResponsePickingDataModLocList>,
                response: Response<ResponsePickingDataModLocList>
            ) {
                val tvTotalTitle : TextView = findViewById(R.id.total_title)
                val tvTotalPesan : TextView = findViewById(R.id.total_pesan)
                val tvTotalAmbil : TextView = findViewById(R.id.total_ambil)

                var totalPesan = 0
                var totalAmbil = 0
                var totalBaris: String? = null

                data = response.body()?.data
                data?.forEach {
                    totalBaris = it.jml_baris.toString()
                    totalPesan = totalPesan.plus(it.qty!!.toInt())
                    totalAmbil = totalAmbil.plus(it.qty_ambil!!.toInt())
                }

                tvTotalTitle.text = "Total $totalBaris Baris"
                tvTotalAmbil.text = totalAmbil.toString()
                tvTotalPesan.text = totalPesan.toString()

                adapterPickingAllModLocList = AdapterPickingAllModLocList(
                    applicationContext, data!!, object : AdapterPickingAllModLocList.OnAdapterListener{
                        override fun OnClick(list: ModelsModLocList) {
                            session.edit()
                                .putString("loc_id", list.from_location.toString())
                                .putString("loc_name", list.loc_name.toString())
                                .putString("loc_cd", list.loc_cd.toString())
                                .apply()
                            startActivity(
                                Intent(applicationContext, PickingModLocItemListActivity::class.java)
                            )
                        }
                    }
                )
                adapterPickingAllModLocList!!.setItemList(data as MutableList<ModelsModLocList>)
                recyclerView?.adapter = adapterPickingAllModLocList
                adapterPickingAllModLocList!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponsePickingDataModLocList>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
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

    private fun search(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterPickingAllModLocList?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val btnLanjut : Button = findViewById(R.id.btn_next)
        val edScanLokasi : TextInputEditText = findViewById(R.id.ed_scanLokasi)
        btnLanjut.setOnClickListener {
            progressDialog!!.show()
            if(edScanLokasi.text!!.isEmpty()){
                progressDialog!!.dismiss()
                edScanLokasi.error = "Scan Lokasi tidak boleh kosong"
            }else{
                val res : RequestApi = ApiServer().koneksiRetrofit().create(
                    RequestApi::class.java
                )
                val row : Call<ResponsePickingCheckScanLoc> =
                    res.pickingAllCheckScanLoc(
                        RequestPickingCheckScanLoc(
                            db_name = session.getString("db_name", null),
                            task = "check_scan_mod_loc_list",
                            loc_cd = edScanLokasi.text.toString(),
                            wave_plan_id = session.getString("wave_plan_id", null)
                        )
                    )
                row.enqueue(object : Callback<ResponsePickingCheckScanLoc>{
                    override fun onResponse(
                        call: Call<ResponsePickingCheckScanLoc>,
                        response: Response<ResponsePickingCheckScanLoc>
                    ) {
                        location = response.body()?.data
                        when(response.body()?.message){
                            "" -> {
                                session.edit()
                                    .putString("loc_id", location!![0].loc_id.toString())
                                    .putString("loc_name", location!![0].loc_name.toString())
                                    .putString("loc_cd", location!![0].loc_cd.toString())
                                    .apply()
                                startActivity(
                                    Intent(applicationContext, PickingModLocItemListActivity::class.java)
                                )
                            }
                            else -> {
                                edScanLokasi.error = response.body()?.message.toString()
                            }
                        }
                        progressDialog!!.dismiss()
                    }

                    override fun onFailure(call: Call<ResponsePickingCheckScanLoc>, t: Throwable) {
                        progressDialog!!.dismiss()
                        Toast.makeText(applicationContext, "Gagal menghubungi Server",
                        Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    private fun removeSharedPreference(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        session.edit()
            .putString("lpn_id", null)
            .putString("pack_type_id", null)
            .putString("pack_type_name", null)
            .apply()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
    }

    private fun backBtnPressed() {
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreference()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PickingModInValLPNActivity::class.java)
            )
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreference()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PickingModInValLPNActivity::class.java)
        )
    }

    companion object {
        var valueScan : TextInputEditText? = null
    }

}