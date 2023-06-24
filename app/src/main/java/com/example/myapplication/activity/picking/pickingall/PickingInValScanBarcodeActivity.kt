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
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.pengambilan.all.AdapterPickingAllScanBarcode
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingGetTmpPick
import com.example.myapplication.models.pengambilan.all.RequestPickingConfirmWave
import com.example.myapplication.models.pengambilan.all.RequestPickingGetTmpPick
import com.example.myapplication.models.pengambilan.all.RequestPickingTmpToWQ
import com.example.myapplication.models.pengambilan.all.RequestPickingUpdateTmpPick
import com.example.myapplication.models.pengambilan.all.models.ModelsMessage
import com.example.myapplication.models.pengambilan.all.models.ModelsTmpPick
import com.example.myapplication.models.pengambilan.all.models.ModelsWQMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingInValScanBarcodeActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var list : List<ModelsTmpPick>? = null
    private var adapterPickingAllScanBarcode : AdapterPickingAllScanBarcode? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var searchView : SearchView? = null
    private var progressDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_in_val_scan_barcode)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("no_so", null)+
                "\n"+session.getString("pack_type_name", null)+": "+session.getString("lpn_id", null)+
                " @"+session.getString("loc_name", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener { search(searchView!!) }

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, PickingScanBarcodeActivity::class.java)
                )
            }else{
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val btnScan : Button = findViewById(R.id.btn_scan_barcode)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        val btnSimpan : Button = findViewById(R.id.btn_pengambilan_simpan)
        btnSimpan.setOnClickListener {
            updateTmpPick()
        }

        initProgressDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@PickingInValScanBarcodeActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
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
                adapterPickingAllScanBarcode?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingGetTmpPick> = res.pickingAllGetTmpPick(
            RequestPickingGetTmpPick(
                db_name = session.getString("db_name", null),
                task = "get_tmp_pick",
                wave_plan_id = session.getString("wave_plan_id", null),
                loc_id = session.getString("loc_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ResponsePickingGetTmpPick>{
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            override fun onResponse(
                call: Call<ResponsePickingGetTmpPick>,
                response: Response<ResponsePickingGetTmpPick>
            ) {
                list = response.body()?.data

                val tvTotalTitle : TextView = findViewById(R.id.total_title)
                val tvTotalPesan : TextView = findViewById(R.id.total_pesan)
                val tvTotalAmbil : TextView = findViewById(R.id.total_ambil)

                var totalPesan = 0
                var totalAmbil = 0
                var totalBaris = list?.size

                list?.forEach{
                    totalAmbil = totalAmbil.plus(it.t_picked_qty!!.toInt())
                    totalPesan = totalPesan.plus(it.t_scan_qty!!.toInt())
                }

                tvTotalTitle.text = "Total $totalBaris Baris"
                tvTotalPesan.text = totalPesan.toString()
                tvTotalAmbil.text = totalAmbil.toString()

                adapterPickingAllScanBarcode = AdapterPickingAllScanBarcode(
                    applicationContext, list!!, object : AdapterPickingAllScanBarcode.OnAdapterListener{
                        override fun onClick(list: ModelsTmpPick) {
                            progressDialog!!.show()
                            var qty : Int = list.t_scan_qty!!.toInt() - list.available_qty!!.toInt()
                            if(qty < 0){
                                qty = 0
                            }
                            progressDialog!!.dismiss()
                            Toast.makeText(applicationContext, list.item_id+" - "+list.item_barcode+"\nLebih Scan : "+qty, Toast.LENGTH_LONG).show()
                        }
                    }
                )
                adapterPickingAllScanBarcode!!.setListItem(list as MutableList<ModelsTmpPick>)
                recyclerView?.adapter = adapterPickingAllScanBarcode
                adapterPickingAllScanBarcode!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponsePickingGetTmpPick>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTmpPick() {
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ModelsMessage> = res.pickingAllUpdateTmpPick(
            RequestPickingUpdateTmpPick(
                db_name = session.getString("db_name", null),
                task = "update_tmp_pick",
                wave_plan_id = session.getString("wave_plan_id", null),
                username = session.getString("username", null),
                from_location = session.getString("loc_id", null)
            )
        )
        row.enqueue(object : Callback<ModelsMessage>{
            override fun onResponse(call: Call<ModelsMessage>, response: Response<ModelsMessage>) {
                val message : String? = response.body()?.message
                if(message == ""){
                    tmpToWQ()
                    /*progressDialog!!.dismiss()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()*/
                }else{
                    progressDialog!!.dismiss()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ModelsMessage>, t: Throwable) {
                //do nothing
                progressDialog!!.dismiss()
            }
        })
    }

    private fun tmpToWQ(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ModelsWQMessage> = res.pickingALlTmpToWQ(
            RequestPickingTmpToWQ(
                db_name = session.getString("db_name", null),
                task = "tmp_to_workqueue",
                wave_plan_id = session.getString("wave_plan_id", null),
                lpn_pack = session.getString("lpn_id", null),
                pack_type = session.getString("pack_type_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ModelsWQMessage>{
            override fun onResponse(call: Call<ModelsWQMessage>, response: Response<ModelsWQMessage>) {
                val wqMessage : String = response.body()?.wq_message.toString()
                if(response.body()?.confirm.toString() == "true"){
                    val rowConfirm : Call<ModelsWQMessage> = res.pickingAllConfirmWave(
                        RequestPickingConfirmWave(
                            db_name = session.getString("db_name", null),
                            task = "confirm_wave",
                            wave_plan_id = session.getString("wave_plan_id", null),
                            outbound_pref_id = session.getString("outbound_pref_id", null)
                        )
                    )
                    rowConfirm.enqueue(object : Callback<ModelsWQMessage>{
                        override fun onResponse(
                            call: Call<ModelsWQMessage>,
                            response: Response<ModelsWQMessage>
                        ) {
                            session.edit()
                                .putString("loc_id", null)
                                .putString("loc_name", null)
                                .putString("loc_cd", null)
                                .putString("lpn_id", null)
                                .putString("pack_type_id", null)
                                .putString("pack_type_name", null)
                                .putString("item_group_id", null)
                                .putString("wave_plan_id", null)
                                .putString("wave_comment", null)
                                .putString("no_so", null)
                                .putString("cust_name", null)
                                .apply()
                            progressDialog!!.dismiss()
                            Toast.makeText(applicationContext,
                                "Barang sudah diambil semua - (Auto Confirm)", Toast.LENGTH_LONG).show()
                            startActivity(
                                Intent(applicationContext, PickingModWaveListActivity::class.java)
                            )
                        }
                        override fun onFailure(call: Call<ModelsWQMessage>, t: Throwable) {
                            progressDialog!!.dismiss()
                        }
                    })
                }else{
                    if(wqMessage == "DONE"){
                        session.edit()
                            .putString("loc_id", null)
                            .putString("loc_name", null)
                            .putString("loc_cd", null)
                            .apply()
                        startActivity(
                            Intent(applicationContext, PickingModLocListActivity::class.java)
                        )
                    }else{
                        startActivity(
                            Intent(applicationContext, PickingModLocItemListActivity::class.java)
                        )
                    }
                    progressDialog!!.dismiss()
                    Toast.makeText(applicationContext,
                        response.body()?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ModelsWQMessage>, t: Throwable) {
                progressDialog!!.dismiss()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        retrieveData()
    }

    private fun backBtnPressed(){
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PickingModLocItemListActivity::class.java)
            )
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(applicationContext, PickingModLocItemListActivity::class.java)
        )
    }

}