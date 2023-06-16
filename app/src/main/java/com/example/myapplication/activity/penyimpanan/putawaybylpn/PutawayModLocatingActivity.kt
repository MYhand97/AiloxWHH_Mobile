package com.example.myapplication.activity.penyimpanan.putawaybylpn

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R

class PutawayModLocatingActivity : AppCompatActivity() {

    private var customDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_putaway_mod_locating)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        initCustomDialog()

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\nLPN: "+ session.getString("lpn_id", null)

        val btnLihatBarang : TextView = findViewById(R.id.putaway_textbtn)
        btnLihatBarang.setOnClickListener {
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayModLPNItemActivity::class.java)
            )
        }

        val btnLokasiAsli : Button = findViewById(R.id.btn_putaway_locating_lokasiawal)
        btnLokasiAsli.setOnClickListener {
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayModInLocActivity::class.java)
            )
        }

        val btnLokasiLain : Button = findViewById(R.id.btn_putaway_locating_lokasilain)
        btnLokasiLain.setOnClickListener {
            customDialog!!.show()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayModLocatingManualActivity::class.java)
            )
        }

        btnBackPressed()
        settingSession()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@PutawayModLocatingActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun settingSession(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val btnLPN : Button = findViewById(R.id.btn_putaway_locating_lokasiawal)
        val errMessage : TextView = findViewById(R.id.putaway_errormsg)
        btnLPN.visibility = View.VISIBLE
        errMessage.visibility = View.GONE

        val err_message = session.getString("err_message", null)
        if(err_message!!.isEmpty()){
            errMessage.visibility = View.GONE
            btnLPN.visibility = View.VISIBLE
            btnLPN.text = session.getString("loc_name", null)
        }else{
            btnLPN.visibility = View.GONE
            errMessage.visibility = View.VISIBLE
            errMessage.text = session.getString("err_message", null)
        }
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_date", MODE_PRIVATE)
            .edit()
            .putString("lpn_id", null)
            .putString("loc_name", null)
            .putString("loc_cd", null)
            .putString("err_message", null)
            .putString("ship_number", null)
            .apply()
    }

    private fun btnBackPressed(){
        val backBtn: ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            customDialog!!.show()
            removeSharedPreferences()
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PutawayInValLpnActivity::class.java)
            )
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialog!!.show()
        removeSharedPreferences()
        customDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PutawayInValLpnActivity::class.java)
        )
        finish()
    }
}