package com.example.myapplication.activity.penyimpanan.putawaybylpn

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
import com.example.myapplication.activity.HomeActivity
import com.example.myapplication.activity.SubMenuActivity
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetAllocation
import com.example.myapplication.data.api.response.penyimpanan.ResponsePutawayGetPalletNumber
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestGetPalletNumber
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetAllocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetAllocation
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutawayInValLpnActivity : AppCompatActivity() {

    private var list: ResponsePutawayGetAllocation? = null
    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_in_val_lpn)

        initViews()
    }

    private fun initViews(){
        valueScan = findViewById(R.id.ed_scanPallet)
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu: TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startActivity(
                    Intent(applicationContext, PutawayScanLPNActivity::class.java)
                )
            }else{
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

        val btnScan: Button = findViewById(R.id.btn_scan_lpn)
        btnScan.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

        initCustomDialog()

        backBtnPressed()
        checkScanValue()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@PutawayInValLpnActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun checkScanValue(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val edScanPallet: TextInputEditText = findViewById(R.id.ed_scanPallet)
        val btnLanjut : Button = findViewById(R.id.btn_next)

        btnLanjut.setOnClickListener {
            customDialog!!.show()
            if(edScanPallet.text!!.isEmpty()){
                customDialog!!.dismiss()
                edScanPallet.error = "Scan Pallet tidak boleh kosong"
            }else{
                val res: RequestApi = ApiServer().koneksiRetrofit().create(
                    RequestApi::class.java
                )
                val row: Call<ResponsePutawayGetPalletNumber> = res.putawayGetPalletNumber(
                    RequestGetPalletNumber(
                        db_name = session.getString("db_name", null),
                        task = "get_pallet_number",
                        lpn_id = edScanPallet.text.toString()
                    )
                )
                row.enqueue(object : Callback<ResponsePutawayGetPalletNumber>{
                    override fun onResponse(
                        call: Call<ResponsePutawayGetPalletNumber>,
                        response: Response<ResponsePutawayGetPalletNumber>
                    ) {
                        when(response.body()?.message){
                            "" -> {
                                val res: RequestApi = ApiServer().koneksiRetrofit().create(
                                    RequestApi::class.java
                                )
                                val row: Call<ResponseDataPutawayGetAllocation> = res.putawayGetAllocation(
                                    RequestPutawayGetAllocation(
                                        db_name = session.getString("db_name", null),
                                        task = "get_allocation",
                                        lpn_id = edScanPallet.text.toString()
                                    )
                                )
                                row.enqueue(object : Callback<ResponseDataPutawayGetAllocation>{
                                    override fun onResponse(
                                        call: Call<ResponseDataPutawayGetAllocation>,
                                        response: Response<ResponseDataPutawayGetAllocation>
                                    ) {
                                        list = response.body()?.data
                                        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                            .edit()
                                            .putString("lpn_id", edScanPallet.text.toString())
                                            .putString("loc_name", list?.loc_name.toString())
                                            .putString("loc_cd", list?.loc_cd.toString())
                                            .putString("err_message", list?.err_message.toString())
                                            .putString("ship_number", list?.ship_number.toString())
                                            .apply()
                                        customDialog!!.dismiss()
                                        startActivity(
                                            Intent(applicationContext, PutawayModLocatingActivity::class.java)
                                        )
                                    }

                                    override fun onFailure(call: Call<ResponseDataPutawayGetAllocation>, t: Throwable) {
                                        customDialog!!.dismiss()
                                    }

                                })
                            }
                            else -> {
                                customDialog!!.dismiss()
                                edScanPallet.error = response.body()?.message.toString()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponsePutawayGetPalletNumber>, t: Throwable) {
                        customDialog!!.dismiss()
                        Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("sub_menu_id", null)
            .putString("sub_menu_title", null)
            .putString("sub_menu_action", null)
            .apply()
    }

    private fun backBtnPressed(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener{
            customDialog!!.show()
            removeSharedPreferences()
            if(checkPageAccess.equals("true")){
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, HomeActivity::class.java)
                )
            }else{
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, SubMenuActivity::class.java)
                )
            }
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        removeSharedPreferences()
        if(checkPageAccess.equals("true")){
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
            )
        }else{
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, SubMenuActivity::class.java)
            )
        }
        finish()
    }

    companion object {
        var valueScan: TextInputEditText? = null
    }
}