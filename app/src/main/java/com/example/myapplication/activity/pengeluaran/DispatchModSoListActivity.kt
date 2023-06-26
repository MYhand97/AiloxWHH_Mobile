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
import com.example.myapplication.adapter.pengeluaran.AdapterDispModSoList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModSoList
import com.example.myapplication.models.pengeluaran.pack.RequestDispModSoList
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModSoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatchModSoListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var adapterDispModSoList : AdapterDispModSoList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressDialog : Dialog? = null
    private var searchView : SearchView? = null
    private var data : List<ModelsDispModSoList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dispatch_mod_so_list)

        initViews()
    }

    private fun initProgressDialog(){
        progressDialog = Dialog(this@DispatchModSoListActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val title : TextView = findViewById(R.id.submenu_title)
        title.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("cust_name", null)

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
        recyclerView!!.layoutManager = linearLayoutManager
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val totalRow : TextView = findViewById(R.id.total_baris)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponseDispModSoList> = res.dispatchPackModSoList(
            RequestDispModSoList(
                db_name = session.getString("db_name", null),
                task = "mod_so_list",
                cust_id = session.getString("cust_id", null)
            )
        )
        row.enqueue(object : Callback<ResponseDispModSoList>{
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseDispModSoList>,
                response: Response<ResponseDispModSoList>
            ) {
                data = response.body()?.data
                val total_baris = data!!.size
                totalRow.text = "Total: $total_baris SO"
                adapterDispModSoList = AdapterDispModSoList(
                    applicationContext, data!!, object : AdapterDispModSoList.OnAdapterListener{
                        override fun onClick(list: ModelsDispModSoList) {
                            progressDialog!!.show()
                            progressDialog!!.dismiss()
                        }
                    })
                adapterDispModSoList!!.setItemList(data as MutableList<ModelsDispModSoList>)
                recyclerView?.adapter = adapterDispModSoList
                adapterDispModSoList!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponseDispModSoList>, t: Throwable) {
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
                adapterDispModSoList?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun removeSharedPreferences(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE).edit()
            .putString("cust_id", null)
            .putString("cust_code", null)
            .putString("cust_name", null)
            .apply()
    }

    private fun backBtnPressed(){
        val btnBack : ConstraintLayout = findViewById(R.id.submenu_backicon)
        btnBack.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreferences()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, DispatchModWaveListActivity::class.java)
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
            Intent(applicationContext, DispatchModWaveListActivity::class.java)
        )
        finish()
    }

}