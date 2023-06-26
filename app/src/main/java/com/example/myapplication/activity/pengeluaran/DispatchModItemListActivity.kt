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
import com.example.myapplication.adapter.pengeluaran.AdapterDispModItemList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModItemList
import com.example.myapplication.models.pengeluaran.pack.RequestDispModItemList
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModItemList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatchModItemListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var adapterDispModItemList : AdapterDispModItemList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressDialog : Dialog? = null
    private var searchView : SearchView? = null
    private var data : List<ModelsDispModItemList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_mod_item_list)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        DispatchModInValLpnActivity.valueScan = findViewById(R.id.ed_scan_pack)

        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title : TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("cust_name", null)+
                "\n"+session.getString("ship_number", null)+
                "@"+session.getString("lpn_id", null)

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

    private fun initProgressDialog(){
        progressDialog = Dialog(this@DispatchModItemListActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val totalRow : TextView = findViewById(R.id.total_baris)
        val QtyTotal : TextView = findViewById(R.id.total_qty)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDispModItemList> = res.dispatchPackModItemList(
            RequestDispModItemList(
                db_name = session.getString("db_name", null),
                task = "mod_item_list",
                wave_plans_id = session.getString("wave_plans_id", null),
                lpn_id = session.getString("lpn_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseDispModItemList>{
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDispModItemList>,
                response: Response<ResponseDispModItemList>
            ) {
                data = response.body()!!.data

                var totalQty = 0
                data?.forEach{
                    totalQty = totalQty.plus(it.pack_qty!!.toInt())
                }

                totalRow.text = "Total: "+data!!.size+" Baris"
                QtyTotal.text = totalQty.toString()

                adapterDispModItemList = AdapterDispModItemList(
                    applicationContext, data!!, object : AdapterDispModItemList.OnAdapterListener{
                        override fun onClick(list: ModelsDispModItemList) {
                            Toast.makeText(applicationContext, list.item_number, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                adapterDispModItemList!!.setItemList(data as MutableList<ModelsDispModItemList>)
                recyclerView?.adapter = adapterDispModItemList
                adapterDispModItemList!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponseDispModItemList>, t: Throwable) {
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
                adapterDispModItemList?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("wave_plans_id", null)
            .putString("lpn_id", null)
            .apply()
    }

    private fun backBtnPressed(){
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, DispatchModInValLpnActivity::class.java)
            )
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreferences()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, DispatchModInValLpnActivity::class.java)
        )
        finish()
    }
}