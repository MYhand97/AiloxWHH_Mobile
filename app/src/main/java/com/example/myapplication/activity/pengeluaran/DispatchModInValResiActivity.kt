package com.example.myapplication.activity.pengeluaran

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
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText

class DispatchModInValResiActivity : AppCompatActivity() {

    private var progressDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_mod_in_val_resi)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        valueScan = findViewById(R.id.ed_scan_resi)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title : TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("cust_name", null)+
                "\n"+session.getString("ship_number", null)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, DispatchScanResiActivity::class.java)
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
        checkScanValue()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@DispatchModInValResiActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val btnLanjut : Button = findViewById(R.id.btn_next)
        val edScanResi : TextInputEditText = findViewById(R.id.ed_scan_resi)

        btnLanjut.setOnClickListener {
            progressDialog!!.show()
            if(edScanResi.text?.isEmpty() == true){
                progressDialog!!.dismiss()
                edScanResi.error = "Nomor Resi tidak boleh kosong"
            }else{
                session.edit()
                    .putString("nomor_resi", edScanResi.text.toString())
                    .apply()
                progressDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, DispatchModInValLpnActivity::class.java)
                )
            }
        }
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("surat_jalan", null)
            .apply()
    }

    private fun backBtnPressed(){
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, DispatchModInValDnActivity::class.java)
            )
            finish()
        }
    }

    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreferences()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, DispatchModInValDnActivity::class.java)
        )
        finish()
    }

    companion object {
        var valueScan : TextInputEditText? = null
    }
}