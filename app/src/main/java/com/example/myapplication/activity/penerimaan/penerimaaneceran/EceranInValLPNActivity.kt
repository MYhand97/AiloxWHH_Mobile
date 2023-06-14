package com.example.myapplication.activity.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.example.myapplication.activity.penerimaan.PenerimaanEceranActivity
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptLPN
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestReleaseRcpt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestSelectLpn
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseLPN
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EceranInValLPNActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_eceran_in_val_lpn)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        valueScan = findViewById(R.id.ed_scanPallet)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu: TextView = findViewById(R.id.submenu_title)
        titleMenu.text = "Penerimaan\n"+session.getString("rcpt_number", null)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startActivity(
                    Intent(applicationContext, EceranScanLPNActivity::class.java)
                )
            } else {
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val btnScan : Button = findViewById(R.id.btn_scan_lpn)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        backBtnPressed()
        checkScanValue()
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val btnLanjut : Button = findViewById(R.id.btn_next)
        val edScanPallet : TextInputEditText = findViewById(R.id.ed_scanPallet)

        btnLanjut.setOnClickListener {
            if(edScanPallet.text?.isEmpty() == true){
                edScanPallet.error = "Scan Pallet tidak boleh kosong"
            }else{
                val res: RequestApi = ApiServer().koneksiRetrofit().create(
                    RequestApi::class.java
                )
                val selectLPN: Call<ResponseRcptLPN> = res.selectLPN(
                    RequestSelectLpn(
                        db_name = session.getString("db_name", null),
                        task = "check_lpn_id",
                        lpn_id = edScanPallet.text.toString()
                    )
                )
                selectLPN.enqueue(object : Callback<ResponseRcptLPN>{
                    override fun onResponse(
                        call: Call<ResponseRcptLPN>,
                        response: Response<ResponseRcptLPN>
                    ) {
                        val message: String? = response.body()?.message
                        val data: List<ResponseLPN>? = response.body()?.data
                        when (message) {
                            "LPN belum terdaftar" -> {
                                edScanPallet.error = "LPN belum terdaftar"
                            }
                            "LPN ada lebih dari 1" -> {
                                edScanPallet.error = "LPN ada lebih dari 1"
                            }
                            "LPN sedang di proses" -> {
                                edScanPallet.error = "LPN sedang di proses"
                            }
                            "LPN sudah di gunakan" -> {
                                edScanPallet.error = "LPN sudah di gunakan"
                            }
                            else -> {
                                getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                    .edit()
                                    .putString("lpn_id", data?.get(0)?.lpn_id)
                                    .apply()
                                startActivity(
                                    Intent(applicationContext, EceranSubMenuActivity::class.java)
                                )
                                //finish()
                                //Toast.makeText(applicationContext, message+" - "+data?.get(0)?.lpn_id, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseRcptLPN>, t: Throwable) {
                        Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
                    }

                })
                //Toast.makeText(applicationContext, edScanPallet.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseRcptStatus()
        removeSharedPreferences()
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("rcpt_header_id", null)
            .putString("rcpt_number", null)
            .apply()
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
            }

            override fun onFailure(call: Call<ResponseRcptHeader>, t: Throwable) {
                //Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun backBtnPressed(){
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            releaseRcptStatus()
            startActivity(
                Intent(applicationContext, PenerimaanEceranActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        releaseRcptStatus()
        startActivity(
            Intent(applicationContext, PenerimaanEceranActivity::class.java)
        )
        finish()
    }

    companion object {
        var valueScan: TextInputEditText? = null
    }
}