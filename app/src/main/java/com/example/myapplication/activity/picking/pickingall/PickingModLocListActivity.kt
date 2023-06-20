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
import com.example.myapplication.models.pengambilan.all.models.ModelsModLocList
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModLocListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var data : List<ModelsModLocList>? = null
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
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("no_so", null)+
                "\n"+session.getString("pack_type_name", null)+": "+session.getString("lpn_id", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
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

                var total_pesan = 0
                var total_ambil = 0
                var total_baris: String? = null

                data = response.body()?.data
                data?.forEach {
                    total_baris = it.jml_baris.toString()
                    total_pesan = total_pesan.plus(it.qty!!.toInt())
                    total_ambil = total_ambil.plus(it.qty_ambil!!.toInt())
                }

                tvTotalTitle.text = "Total $total_baris Baris"
                tvTotalAmbil.text = total_ambil.toString()
                tvTotalPesan.text = total_pesan.toString()

                adapterPickingAllModLocList = AdapterPickingAllModLocList(
                    applicationContext, data!!, object : AdapterPickingAllModLocList.OnAdapterListener{
                        override fun OnClick(list: ModelsModLocList) {
                            progressDialog!!.show()
                            Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).show()
                            progressDialog!!.dismiss()
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
                        when(response.body()?.message){
                            "" -> {
                                Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                edScanLokasi.error = response.body()?.message.toString()
                            }
                        }
                        progressDialog!!.dismiss()
                    }

                    override fun onFailure(call: Call<ResponsePickingCheckScanLoc>, t: Throwable) {
                        TODO("Not yet implemented")
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

    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreference()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PickingModInValLPNActivity::class.java)
        )
    }

}