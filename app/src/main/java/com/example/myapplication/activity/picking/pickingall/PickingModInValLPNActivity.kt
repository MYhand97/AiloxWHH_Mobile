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
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModInValLPN
import com.example.myapplication.models.pengambilan.all.RequestPickingModInValLPN
import com.example.myapplication.models.pengambilan.all.models.ModelsPackTypeList
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModInValLPNActivity : AppCompatActivity() {

    private var list : List<ModelsPackTypeList>? = null
    private var progressDialog : Dialog? = null

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
        checkScanValue()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@PickingModInValLPNActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
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
                val row : Call<ResponsePickingDataModInValLPN> =
                    res.pickingAllCheckScanLPN(
                        RequestPickingModInValLPN(
                            db_name = session.getString("db_name", null),
                            task = "check_scan_label_pack",
                            lpn_id = edScanPallet.text.toString(),
                            wave_plan_id = session.getString("wave_plan_id", null)
                        )
                    )
                row.enqueue(object : Callback<ResponsePickingDataModInValLPN>{
                    override fun onResponse(
                        call: Call<ResponsePickingDataModInValLPN>,
                        response: Response<ResponsePickingDataModInValLPN>
                    ) {
                        val message = response.body()!!.message
                        list = response.body()!!.pack_list
                        when(message){
                            "" -> {
                                session.edit()
                                    .putString("lpn_id", edScanPallet.text.toString())
                                    .putString("pack_type_id", list!![0].pack_type_id.toString())
                                    .putString("pack_type_name", list!![0].pack_type_name.toString())
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
                        call: Call<ResponsePickingDataModInValLPN>,
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
}