package com.example.myapplication.activity.pengeluaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import com.example.myapplication.R
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class DispatchScanDnActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView : ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_scan_dn)

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
        DispatchModInValDnActivity.valueScan!!.text =
            Editable.Factory.getInstance().newEditable(p0!!.text)
        onBackPressed()
    }
}