package com.example.myapplication.activity.picking.pickingall

import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class PickingScanLocListActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

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

    override fun handleResult(p0: Result?) {
        PickingModLocListActivity.valueScan!!.text =
            Editable.Factory.getInstance().newEditable(p0!!.text)
        onBackPressed()
    }
}