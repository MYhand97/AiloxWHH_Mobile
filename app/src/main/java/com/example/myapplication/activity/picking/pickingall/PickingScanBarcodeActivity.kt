package com.example.myapplication.activity.picking.pickingall

import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.models.pengambilan.all.RequestPickingPostToTmpPick
import com.example.myapplication.models.pengambilan.all.models.ModelsMessage
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingScanBarcodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView : ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

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

    override fun handleResult(rawResult: Result?) {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ModelsMessage> = res.pickingAllPostToTmp(
            RequestPickingPostToTmpPick(
                db_name = session.getString("db_name", null),
                task = "post_tmp_pick",
                item_barcode = rawResult!!.text.toString(),
                loc_id = session.getString("loc_id", null),
                wave_plan_id = session.getString("wave_plan_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ModelsMessage>{
            override fun onResponse(call: Call<ModelsMessage>, response: Response<ModelsMessage>) {
                val message : String = response.body()?.message.toString()
                if(message == ""){
                    onBackPressed()
                }else{
                    Toast
                        .makeText(applicationContext, message, Toast.LENGTH_LONG)
                        .show()
                    onBackPressed()
                }
            }
            override fun onFailure(call: Call<ModelsMessage>, t: Throwable) {
                onBackPressed()
            }

        })
    }
}