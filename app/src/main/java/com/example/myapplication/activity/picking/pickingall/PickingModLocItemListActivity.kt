package com.example.myapplication.activity.picking.pickingall

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.pengambilan.all.AdapterPickingAllModLocItemList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModLocItemList
import com.example.myapplication.models.pengambilan.all.RequestPickingModLocItemList
import com.example.myapplication.models.pengambilan.all.models.ModelsLocItemList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModLocItemListActivity : AppCompatActivity() {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var data : List<ModelsLocItemList>? = null
    private var adapterPickingAllModLocItemList : AdapterPickingAllModLocItemList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var searchView : SearchView? = null
    private var progressDialog : Dialog? = null
    private var itemDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_mod_loc_item_list)

        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)+
                "\n"+session.getString("no_so", null)+
                "\n"+session.getString("pack_type_name", null)+": "+session.getString("lpn_id", null)+
                " @"+session.getString("loc_name", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
        }

        initProgressDialog()
        initItemDialog()
        setupRecyclerView()

        backBtnPressed()
        swipeRefresh()
        search(searchView!!)
        retrieveData()
    }

    private fun initItemDialog(){
        itemDialog = Dialog(this@PickingModLocItemListActivity)
        itemDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        itemDialog!!.setContentView(R.layout.dialog_item)
        itemDialog!!.setCancelable(true)
    }

    @SuppressLint("SetTextI18n")
    private fun openItemDialog(){
        val title : TextView = itemDialog!!.findViewById(R.id.dialog_item_title)
        val msg : TextView = itemDialog!!.findViewById(R.id.dialog_item_msg)

        title.text = "Pengambilan"
        msg.text = "Masukan jumlah yang akan diambil"

        val btnOK : Button = itemDialog!!.findViewById(R.id.btn_ok)
        val btnCancel : Button = itemDialog!!.findViewById(R.id.btn_cancel_logout)

        itemDialog!!.show()

        btnOK.text = "AMBIL"
        btnOK.setOnClickListener {

        }
        btnCancel.setOnClickListener {
            itemDialog!!.dismiss()
        }
    }

    private fun initProgressDialog() {
        progressDialog = Dialog(this@PickingModLocItemListActivity)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(R.layout.dialog_progress)
        progressDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }


    private fun swipeRefresh(){
        searchView = findViewById(R.id.pp1_searchview)
        swipeRefreshLayout?.setOnRefreshListener {
            search(searchView!!)
            retrieveData()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun retrieveData(){
        progressDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingDataModLocItemList> = res.pickingAllModLocItemList(
            RequestPickingModLocItemList(
                db_name = session.getString("db_name", null),
                task = "mod_loc_item_list",
                outbound_pref_id = session.getString("outbound_pref_id", null),
                loc_id =  session.getString("loc_id", null),
                wave_plan_id = session.getString("wave_plan_id", null)
            )
        )
        row.enqueue(object : Callback<ResponsePickingDataModLocItemList>{
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponsePickingDataModLocItemList>,
                response: Response<ResponsePickingDataModLocItemList>
            ) {
                val tvTotalTitle : TextView = findViewById(R.id.total_title)
                val tvTotalPesan : TextView = findViewById(R.id.total_pesan)
                val tvTotalAmbil : TextView = findViewById(R.id.total_ambil)

                var totalPesan = 0
                var totalAmbil = 0
                var totalBaris : String? = null

                data = response.body()?.data
                data?.forEach{
                    totalBaris = it.jml_baris.toString()
                    totalAmbil = totalAmbil.plus(it.qty_ambil!!.toInt())
                    totalPesan = totalPesan.plus(it.qty!!.toInt())
                }

                tvTotalTitle.text = "Total $totalBaris Baris"
                tvTotalPesan.text = totalPesan.toString()
                tvTotalAmbil.text = totalAmbil.toString()

                adapterPickingAllModLocItemList = AdapterPickingAllModLocItemList(
                    applicationContext, data!!, object : AdapterPickingAllModLocItemList.OnAdapterListener{
                        override fun onClick(list: ModelsLocItemList) {
                            openItemDialog()
                        }
                    }
                )
                adapterPickingAllModLocItemList!!.setItemList(data as MutableList<ModelsLocItemList>)
                recyclerView?.adapter = adapterPickingAllModLocItemList
                adapterPickingAllModLocItemList!!.notifyDataSetChanged()
                progressDialog!!.dismiss()
            }
            override fun onFailure(call: Call<ResponsePickingDataModLocItemList>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun search(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapterPickingAllModLocItemList?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun removeSharedPreference(){
        getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
            .edit()
            .putString("loc_id", null)
            .putString("loc_name", null)
            .putString("loc_cd", null)
            .apply()
    }

    private fun backBtnPressed(){
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            progressDialog!!.show()
            removeSharedPreference()
            progressDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, PickingModLocListActivity::class.java
                )
            )
            finish()
        }
    }

    override fun onBackPressed() {
        progressDialog!!.show()
        removeSharedPreference()
        progressDialog!!.dismiss()
        startActivity(
            Intent(applicationContext, PickingModLocListActivity::class.java)
        )
        finish()
    }

}