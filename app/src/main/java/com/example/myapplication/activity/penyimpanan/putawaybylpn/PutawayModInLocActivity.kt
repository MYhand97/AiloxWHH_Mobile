package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataCheckLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayCheckLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayCheckLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetAllocation
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutawayModInLocActivity : AppCompatActivity() {

    private var list: List<ResponsePutawayCheckLocation>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_in_locating)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)+
                "\nLokasi: "+ session.getString("loc_name", null)

        valueScan = findViewById(R.id.ed_scanPallet)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, PutawayScanModInLocActivity::class.java)
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

    private fun checkScanValue() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val edScanLokasi : TextInputEditText = findViewById(R.id.ed_scanPallet)
        val btnLanjut : Button = findViewById(R.id.btn_next)

        btnLanjut.setOnClickListener {
            if(edScanLokasi.text!!.isEmpty()){
                edScanLokasi.error = "Scan Lokasi tidak boleh kosong"
            }else{
                if(edScanLokasi.text.toString() == session.getString("loc_cd", null).toString()){
                    val res : RequestApi = ApiServer().koneksiRetrofit().create(
                        RequestApi::class.java
                    )
                    val row : Call<ResponseDataCheckLocation> = res.putawayCheckLocation(
                        RequestPutawayCheckLocation(
                            db_name = session.getString("db_name", null),
                            task = "check_location",
                            loc_cd = edScanLokasi.text.toString()
                        )
                    )
                    row.enqueue(object : Callback<ResponseDataCheckLocation>{
                        override fun onResponse(
                            call: Call<ResponseDataCheckLocation>,
                            response: Response<ResponseDataCheckLocation>
                        ) {
                            val message = response.body()?.message
                            list = response.body()?.data
                            when(message){
                                "" -> {
                                    Toast.makeText(applicationContext, list?.get(0)?.vLocName.toString(), Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    edScanLokasi.error = message.toString()
                                }
                            }
                        }
                        override fun onFailure(call: Call<ResponseDataCheckLocation>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }else{
                    edScanLokasi.error = "Lokasi salah, simpan barang di lokasi "+session.getString("loc_name", null)
                }
            }
        }
    }

    private fun backBtnPressed() {
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            startActivity(
                Intent(applicationContext, PutawayModLocatingActivity::class.java)
            )
            finish()
        }
    }


    override fun onBackPressed() {
        startActivity(
            Intent(applicationContext, PutawayModLocatingActivity::class.java)
        )
        finish()
    }

    companion object {
        var valueScan: TextInputEditText? = null
    }
}