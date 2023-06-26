package com.example.myapplication.activity.pengeluaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.models.pengeluaran.pack.RequestDispScanValLpn
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispMessage
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispMessageWave
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatchScanValLpnActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_scan_val_lpn)

        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ModelsDispMessageWave> = res.dispatchPackScanValLpn(
            RequestDispScanValLpn(
                db_name = session.getString("db_name", null),
                task = "check_mod_in_val_lpn",
                ship_header_id = session.getString("ship_header_id", null),
                lpn_id = p0!!.text.toString()
            )
        )
        row.enqueue(object : Callback<ModelsDispMessageWave>{
            override fun onResponse(
                call: Call<ModelsDispMessageWave>,
                response: Response<ModelsDispMessageWave>
            ) {
                val message : String = response.body()?.message.toString()

                if(message == ""){
                    onBackPressed()
                }else{
                    DispatchModInValLpnActivity.valueScan?.text =
                        Editable.Factory.getInstance().newEditable(p0.text)
                    DispatchModInValLpnActivity.valueScan?.error = message
                    onBackPressed()
                }
            }
            override fun onFailure(call: Call<ModelsDispMessageWave>, t: Throwable) {
                onBackPressed()
                Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}