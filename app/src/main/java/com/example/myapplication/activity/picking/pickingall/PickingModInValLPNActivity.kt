package com.example.myapplication.activity.picking.pickingall

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingModInValLPN
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingPackTypeList
import com.example.myapplication.models.pengambilan.all.RequestPickingModInValLPN
import com.example.myapplication.models.pengambilan.all.RequestPickingPackTypeList
import com.example.myapplication.models.pengambilan.all.models.ModelsPackTypeList
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModInValLPNActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var progressDialog : Dialog? = null
    private var spinnerPackType : Spinner? = null

    private var packList : List<ModelsPackTypeList>? = null
    private var listPackTypeId = ArrayList<String>()
    private var listPackTypeName = ArrayList<String>()
    private var packTypeId : String? = null
    private var packTypeName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_mod_in_val_lpn)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        valueScan = findViewById(R.id.ed_scanPallet)

        spinnerPackType = findViewById(R.id.spinner_pack_list)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("no_so", null)

        initProgressDialog()

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, PickingScanLPNActivity::class.java)
                )
            }else{
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

        val btnScan : Button = findViewById(R.id.btn_scan_lpn)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        backBtnPressed()
        spinnerPackType()
        checkScanValue()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@PickingModInValLPNActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun spinnerPackType(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingPackTypeList> = res.pickingAllPackTypeList(
            RequestPickingPackTypeList(
                db_name = session.getString("db_name", null),
                task = "get_pack_type"
            )
        )
        row.enqueue(object : Callback<ResponsePickingPackTypeList>{
            override fun onResponse(
                call: Call<ResponsePickingPackTypeList>,
                response: Response<ResponsePickingPackTypeList>
            ) {
                packList = response.body()?.pack_list
                packList?.forEach{
                    listPackTypeId.add(it.pack_type_id.toString())
                    listPackTypeName.add(it.pack_type_name.toString())
                }
                spinnerPackType!!.onItemSelectedListener = this@PickingModInValLPNActivity
                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, listPackTypeName)
                spinnerPackType!!.adapter = adapter
            }

            override fun onFailure(call: Call<ResponsePickingPackTypeList>, t: Throwable) {
                // do nothing
            }

        })
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val btnLanjut : Button = findViewById(R.id.btn_next)
        val edScanPallet : TextInputEditText = findViewById(R.id.ed_scanPallet)
        btnLanjut.setOnClickListener {
            progressDialog!!.show()
            if(edScanPallet.text!!.isEmpty()){
                progressDialog!!.dismiss()
                edScanPallet.error = "Scan Label Pack tidak boleh kosong"
            }else{
                val res : RequestApi = ApiServer().koneksiRetrofit().create(
                    RequestApi::class.java
                )
                val row : Call<ResponsePickingModInValLPN> =
                    res.pickingAllCheckScanLPN(
                        RequestPickingModInValLPN(
                            db_name = session.getString("db_name", null),
                            task = "check_scan_label_pack",
                            lpn_id = edScanPallet.text.toString(),
                            wave_plan_id = session.getString("wave_plan_id", null)
                        )
                    )
                row.enqueue(object : Callback<ResponsePickingModInValLPN>{
                    override fun onResponse(
                        call: Call<ResponsePickingModInValLPN>,
                        response: Response<ResponsePickingModInValLPN>
                    ) {
                        when(val message = response.body()!!.message){
                            "" -> {
                                session.edit()
                                    .putString("lpn_id", edScanPallet.text.toString())
                                    .putString("pack_type_id", packTypeId)
                                    .putString("pack_type_name", packTypeName)
                                    .apply()
                                progressDialog!!.dismiss()
                                startActivity(
                                    Intent(applicationContext, PickingModLocListActivity::class.java)
                                )
                            }
                            else -> {
                                progressDialog!!.dismiss()
                                edScanPallet.error = message.toString()
                            }
                        }
                    }
                    override fun onFailure(
                        call: Call<ResponsePickingModInValLPN>,
                        t: Throwable
                    ) {
                        progressDialog!!.dismiss()
                        Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun removeSharedPreference(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("item_group_id", null)
            .putString("wave_plan_id", null)
            .putString("wave_comment", null)
            .putString("no_so", null)
            .putString("cust_name", null)
            .apply()
    }

    private fun backBtnPressed(){
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreference()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PickingModWaveListActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreference()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PickingModWaveListActivity::class.java)
        )
        finish()
    }

    companion object {
        var valueScan : TextInputEditText? = null
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
        if(p0?.selectedItem == spinnerPackType?.selectedItem){
            packTypeId = listPackTypeId[p2]
            packTypeName = listPackTypeName[p2]
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}