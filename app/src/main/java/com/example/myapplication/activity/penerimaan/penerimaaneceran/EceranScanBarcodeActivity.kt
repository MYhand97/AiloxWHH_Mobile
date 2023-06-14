package com.example.myapplication.activity.penerimaan.penerimaaneceran

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.penerimaan.ResponseEceranPostTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestPostTmpReceive
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EceranScanBarcodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    lateinit var requestCamera: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //setContentView(R.layout.activity_eceran_scan_lpn)

        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)

        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(applicationContext, "Camera Permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Camera Permission not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requestCamera.launch(Manifest.permission.CAMERA)
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row: Call<ResponseEceranPostTmpReceive> = res.eceranPostTmpReceive(
            RequestPostTmpReceive(
                db_name = session.getString("db_name", null),
                task = "post_tmp_receive",
                item_barcode = rawResult!!.text.toString(),
                rcpt_header_id = session.getString("rcpt_header_id", null),
                username = session.getString("username", null)
            )
        )
        row.enqueue(object : Callback<ResponseEceranPostTmpReceive>{
            override fun onResponse(
                call: Call<ResponseEceranPostTmpReceive>,
                response: Response<ResponseEceranPostTmpReceive>
            ) {
                val message: String = response.body()?.message.toString()
                if(message == ""){
                    onBackPressed()
                }else{
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }

            override fun onFailure(call: Call<ResponseEceranPostTmpReceive>, t: Throwable) {
                //Toast.makeText(applicationContext, "Something error", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })
    }
}