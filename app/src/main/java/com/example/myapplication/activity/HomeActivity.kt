package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.activity.penerimaan.PenerimaanEceranActivity
import com.example.myapplication.activity.penyimpanan.putawaybylpn.PutawayInValLpnActivity
import com.example.myapplication.activity.picking.pickingall.PickingModWaveListActivity
import com.example.myapplication.adapter.AdapterGetMainMenu
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseGetMainMenu
import com.example.myapplication.data.api.response.ResponseGetSubMenu
import com.example.myapplication.data.api.response.ResponseUserLogin
import com.example.myapplication.models.RequestMainMenu
import com.example.myapplication.models.RequestModelsUsers
import com.example.myapplication.models.RequestSubMenu
import com.example.myapplication.models.ResponseMainMenu
import com.example.myapplication.models.ResponseSubMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var list: List<ResponseMainMenu>? = null
    private var listsubmenuPenerimaan: List<ResponseSubMenu>? = null
    private var listsubmenuPenyimpanan: List<ResponseSubMenu>? = null
    private var listsubmenuPengambilan: List<ResponseSubMenu>? = null
    private var listsubmenuPengeluaran: List<ResponseSubMenu>? = null
    private var adapterGetMainMenu: AdapterGetMainMenu? = null
    //private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var customDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        if(session.getString("username", null) != null){
            initViews()
        }else{
            val dbData: RequestApi = ApiServer().koneksiRetrofit().create(
                RequestApi::class.java
            )
            val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            val userLogout: Call<ResponseUserLogin> = dbData.userLogout(
                RequestModelsUsers(
                    db_name = session.getString("db_name", null),
                    username = session.getString("username", null),
                    password = "transmarco"
                )
            )
            userLogout.enqueue(object: Callback<ResponseUserLogin>{
                override fun onResponse(
                    call: Call<ResponseUserLogin>,
                    response: Response<ResponseUserLogin>
                ) {
                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                        .edit()
                        .putString("db_name", null)
                        .putString("userid", null)
                        .putString("username", null)
                        .putString("user_fullname", null)
                        .putString("user_group_id", null)
                        .putString("is_login", null)
                        .apply()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    Toast.makeText(applicationContext, "Ailox : Anda Telah Logout", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {

                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        retrieveMainMenu()
        initCustomDialog()
        userLogout()
    }

    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val messageText: TextView = findViewById(R.id.textView2)
        messageText.text = session.getString("user_fullname",null)

        botNavMenu()
        setupRecycleView()
        retrieveMainMenu()
        initCustomDialog()
        userLogout()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@HomeActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_logout)
        customDialog!!.setCancelable(true)

        val btnLogout: Button = customDialog!!.findViewById(R.id.btn_logout)
        val btnCancelLogout: Button = customDialog!!.findViewById(R.id.btn_cancel_logout)

        val dbData: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        btnLogout.setOnClickListener {
            val userLogout: Call<ResponseUserLogin> = dbData.userLogout(
                RequestModelsUsers(
                    db_name = session.getString("db_name", null),
                    username = session.getString("username", null),
                    password = "transmarco"
                )
            )
            userLogout.enqueue(object: Callback<ResponseUserLogin>{
                override fun onResponse(
                    call: Call<ResponseUserLogin>,
                    response: Response<ResponseUserLogin>
                ) {
                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                        .edit()
                        .putString("db_name", null)
                        .putString("userid", null)
                        .putString("username", null)
                        .putString("user_fullname", null)
                        .putString("user_group_id", null)
                        .putString("is_login", null)
                        .apply()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    customDialog!!.dismiss()
                    Toast.makeText(applicationContext, "Ailox : Anda Telah Logout", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {

                }
            })
        }
        btnCancelLogout.setOnClickListener {
            customDialog!!.dismiss()
        }
    }

    private fun userLogout(){
        val iconLogout: ConstraintLayout = findViewById(R.id.logout_icon)
        iconLogout.setOnClickListener{
            customDialog!!.show()
        }
    }

    override fun onBackPressed() {
        customDialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        val dbData: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val userLogout: Call<ResponseUserLogin> = dbData.userLogout(
            RequestModelsUsers(
                db_name = session.getString("db_name", null),
                username = session.getString("username", null),
                password = "transmarco"
            )
        )
        userLogout.enqueue(object: Callback<ResponseUserLogin>{
            override fun onResponse(
                call: Call<ResponseUserLogin>,
                response: Response<ResponseUserLogin>
            ) {
                getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                    .edit()
                    .putString("db_name", null)
                    .putString("userid", null)
                    .putString("username", null)
                    .putString("user_fullname", null)
                    .putString("user_group_id", null)
                    .putString("is_login", null)
                    .apply()
                Toast.makeText(applicationContext, "Ailox : Anda Telah Logout", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {

            }
        })
    }

    private fun setupRecycleView(){
        recyclerView = findViewById(R.id.my_recycleview_menu)
        linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveMainMenu(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetMainMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetMainMenu> = retGetMainMenu.getMainMenu(
            RequestMainMenu(
                db_name = session.getString("db_name",null)
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetMainMenu>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseGetMainMenu>,
                response: Response<ResponseGetMainMenu>
            ) {
                list = response.body()?.data

                adapterGetMainMenu = AdapterGetMainMenu(applicationContext, list!!, object : AdapterGetMainMenu.OnAdapterListener{
                    override fun onClick(list: ResponseMainMenu) {
                        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                            .edit()
                            .putString("main_menu_id", list.master_sub_menu_id)
                            .putString("main_menu_title", list.sub_menu_title)
                            .apply()
                        startActivity(
                            Intent(applicationContext, SubMenuActivity::class.java)
                        )
                    }
                })

                recyclerView?.adapter = adapterGetMainMenu
                adapterGetMainMenu!!.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<ResponseGetMainMenu>, t: Throwable) {
                Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSubMenuPenerimaan(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetSubMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetSubMenu> = retGetSubMenu.getSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name", null),
                master_sub_menu_id = "180"
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetSubMenu>{
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                listsubmenuPenerimaan = response.body()?.data
            }
            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {

            }
        })
    }

    private fun getSubMenuPenyimpanan(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetSubMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetSubMenu> = retGetSubMenu.getSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name", null),
                master_sub_menu_id = "181"
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetSubMenu>{
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                listsubmenuPenyimpanan = response.body()?.data
            }
            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {

            }
        })
    }

    private fun getSubMenuPengambilan(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetSubMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetSubMenu> = retGetSubMenu.getSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name",null),
                master_sub_menu_id = "182"
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetSubMenu>{
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                listsubmenuPengambilan = response.body()?.data
            }
            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {

            }
        })
    }

    private fun getSubMenuPengeluaran(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val retGetSubMenu: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val tampilData: Call<ResponseGetSubMenu> = retGetSubMenu.getSubMenu(
            RequestSubMenu(
                db_name = session.getString("db_name", null),
                master_sub_menu_id = "183"
            )
        )
        tampilData.enqueue(object : Callback<ResponseGetSubMenu>{
            override fun onResponse(
                call: Call<ResponseGetSubMenu>,
                response: Response<ResponseGetSubMenu>
            ) {
                listsubmenuPengeluaran = response.body()?.data
            }
            override fun onFailure(call: Call<ResponseGetSubMenu>, t: Throwable) {

            }
        })
    }

    private fun botNavMenu(){

        getSubMenuPenerimaan()
        getSubMenuPenyimpanan()
        getSubMenuPengambilan()
        getSubMenuPengeluaran()

        val botBtnPenerimaan: ConstraintLayout = findViewById(R.id.penerimaan_group)
        val botBtnPenyimpanan: ConstraintLayout = findViewById(R.id.penyimpanan_group)
        val botBtnPengambilan: ConstraintLayout = findViewById(R.id.pengambilan_group)
        val botBtnPengeluaran: ConstraintLayout = findViewById(R.id.pengeluaran_group)

        botBtnPenerimaan.setOnClickListener{
            if(listsubmenuPenerimaan?.isEmpty() == false){
                val penerimaanMenu = PopupMenu(this@HomeActivity, botBtnPenerimaan)
                penerimaanMenu.menuInflater.inflate(R.menu.menu_penerimaan, penerimaanMenu.menu)
                for(listsubmenus in listsubmenuPenerimaan!!){
                    penerimaanMenu.menu.add(listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.sub_menu_title.toString())
                    penerimaanMenu.setOnMenuItemClickListener { item ->
                        when (item!!.itemId) {
                            item.itemId ->
                                if(listsubmenuPenerimaan!![item.itemId].sub_menu_action.toString() == "PenerimaanEceranActivity"){
                                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                        .edit()
                                        .putString("fromBotNav", "true")
                                        .putString("sub_menu_id", listsubmenuPenerimaan!![item.itemId].sub_menu_id)
                                        .putString("sub_menu_title", listsubmenuPenerimaan!![item.itemId].sub_menu_title)
                                        .putString("sub_menu_action", listsubmenuPenerimaan!![item.itemId].sub_menu_action)
                                        .apply()
                                    startActivity(
                                        Intent(applicationContext, PenerimaanEceranActivity::class.java)
                                    )
                                }else{
                                    Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                                }
                        }
                        true
                    }
                }
                penerimaanMenu.show()
            }else{
                Toast.makeText(applicationContext, "Menu tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
        botBtnPenyimpanan.setOnClickListener{
            if(listsubmenuPenyimpanan?.isEmpty() == false){
                val penyimpananMenu = PopupMenu(this@HomeActivity, botBtnPenyimpanan)
                penyimpananMenu.menuInflater.inflate(R.menu.menu_penyimpanan, penyimpananMenu.menu)
                for(listsubmenus in listsubmenuPenyimpanan!!){
                    penyimpananMenu.menu.add(listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.sub_menu_title.toString())
                    penyimpananMenu.setOnMenuItemClickListener { item ->
                        when (item!!.itemId) {
                            item.itemId ->
                                if(listsubmenuPenyimpanan!![item.itemId].sub_menu_action.toString() == "PutawayInValLpnActivity"){
                                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                        .edit()
                                        .putString("fromBotNav", "true")
                                        .putString("sub_menu_id", listsubmenuPenyimpanan!![item.itemId].sub_menu_id)
                                        .putString("sub_menu_title", listsubmenuPenyimpanan!![item.itemId].sub_menu_title)
                                        .putString("sub_menu_action", listsubmenuPenyimpanan!![item.itemId].sub_menu_action)
                                        .apply()
                                    startActivity(
                                        Intent(applicationContext, PutawayInValLpnActivity::class.java)
                                    )
                                }else{
                                    Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                                }
                        }
                        true
                    }
                }
                penyimpananMenu.show()
            }else{
                Toast.makeText(applicationContext, "Menu tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
        botBtnPengambilan.setOnClickListener{
            if(listsubmenuPengambilan?.isEmpty() == false){
                val pengambilanMenu = PopupMenu(this@HomeActivity, botBtnPengambilan)
                pengambilanMenu.menuInflater.inflate(R.menu.menu_pengambilan, pengambilanMenu.menu)
                for (listsubmenus in listsubmenuPengambilan!!){
                    pengambilanMenu.menu.add(listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.sub_menu_title.toString())
                    pengambilanMenu.setOnMenuItemClickListener { item ->
                        when (item!!.itemId) {
                            item.itemId ->
                                if(listsubmenuPengambilan!![item.itemId].sub_menu_action.toString() == "PickingAllModWaveList"){
                                    getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
                                        .edit()
                                        .putString("fromBotNav", "true")
                                        .putString("sub_menu_id", listsubmenuPengambilan!![item.itemId].sub_menu_id)
                                        .putString("sub_menu_title", listsubmenuPengambilan!![item.itemId].sub_menu_title)
                                        .putString("sub_menu_action", listsubmenuPengambilan!![item.itemId].sub_menu_action)
                                        .apply()
                                    startActivity(
                                        Intent(applicationContext, PickingModWaveListActivity::class.java)
                                    )
                                }else{
                                    Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                                }
                        }
                        true
                    }
                }
                pengambilanMenu.show()
            }else{
                Toast.makeText(applicationContext, "Menu tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
        botBtnPengeluaran.setOnClickListener{
            if(listsubmenuPengeluaran?.isEmpty() == false){
                val pengeluaranMenu = PopupMenu(this@HomeActivity, botBtnPengeluaran)
                pengeluaranMenu.menuInflater.inflate(R.menu.menu_pengeluaran, pengeluaranMenu.menu)
                for(listsubmenus in listsubmenuPengeluaran!!){
                    pengeluaranMenu.menu.add(listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.rows!!, listsubmenus.sub_menu_title.toString())
                    pengeluaranMenu.setOnMenuItemClickListener { item ->
                        when (item!!.itemId) {
                            item.itemId ->
                                Toast.makeText(applicationContext, "Cannot find any action", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                }
                pengeluaranMenu.show()
            }else{
                Toast.makeText(applicationContext, "Menu tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}