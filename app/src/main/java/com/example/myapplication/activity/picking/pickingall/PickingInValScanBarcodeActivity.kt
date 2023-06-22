package com.example.myapplication.activity.picking.pickingall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PickingInValScanBarcodeActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_in_val_scan_barcode)
    }
}