package com.example.myapplication.activity.pengeluaran

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.activity.HomeActivity
import com.example.myapplication.activity.SubMenuActivity
import com.example.myapplication.adapter.pengeluaran.AdapterDispModWaveList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModWaveList
import com.example.myapplication.models.pengeluaran.pack.RequestDispModWaveList
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModWaveList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatchModWaveListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var data : List<ModelsDispModWaveList>? = null
    private var adapterDispModWaveList : AdapterDispModWaveList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var searchView : SearchView? = null

    private var progressDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_mod_wave_list)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@DispatchModWaveListActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title : TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
        }

        initProgressDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDispModWaveList> = res.dispatchPackModWaveList(
            RequestDispModWaveList(
                db_name = session.getString("db_name", null),
                task = "mod_wave_list"
            )
        )
        row.enqueue(object : Callback<ResponseDispModWaveList>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDispModWaveList>,
                response: Response<ResponseDispModWaveList>
            ) {
                data = response.body()?.data
                adapterDispModWaveList = AdapterDispModWaveList(
                    applicationContext, data!!, object : AdapterDispModWaveList.OnAdapterListener{
                        override fun onClick(list: ModelsDispModWaveList) {
                            progressDialog!!.show()
                            session.edit()
                                .putString("cust_id", list.ship_cust_id.toString())
                                .putString("cust_code", list.cust_code.toString())
                                .putString("cust_name", list.cust_name.toString())
                                .apply()
                            startActivity(
                                Intent(applicationContext, DispatchModSoListActivity::class.java)
                            )
                            progressDialog!!.dismiss()
                        }
                    })
                adapterDispModWaveList!!.setItemList(data as MutableList<ModelsDispModWaveList>)
                recyclerView?.adapter = adapterDispModWaveList
                adapterDispModWaveList!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponseDispModWaveList>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun swipeRefresh(){
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun search(searchView : SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterDispModWaveList?.filter?.filter(newText)
                return true
            }
        })
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
        val checkPage = session.getString("fromBotNav", null)
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            if(checkPage.equals("true")){
                removeSharedPreferences()
                progressDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, HomeActivity::class.java)
                )
            }else{
                removeSharedPreferences()
                progressDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, SubMenuActivity::class.java)
                )
            }
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPage = session.getString("fromBotNav", null)
        if(checkPage.equals("true")){
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
            )
        }else{
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, SubMenuActivity::class.java)
            )
        }
        finish()
    }

}