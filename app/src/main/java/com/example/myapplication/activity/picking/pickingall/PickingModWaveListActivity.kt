package com.example.myapplication.activity.picking.pickingall

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
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
import com.example.myapplication.adapter.pengambilan.all.AdapterPickingAllModWaveList
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModWaveList
import com.example.myapplication.models.pengambilan.all.RequestPickingModWaveList
import com.example.myapplication.models.pengambilan.all.models.ModelsItemGroup
import com.example.myapplication.models.pengambilan.all.models.ModelsWaveList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickingModWaveListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var wavelist : List<ModelsWaveList>? = null
    private var itemgroup : List<ModelsItemGroup>? = null
    private var adapterPickingAllModWaveList : AdapterPickingAllModWaveList? = null
    private var swipeRefreshLayout : SwipeRefreshLayout? = null
    private var progressBar : ProgressBar? = null
    private var searchView : SearchView? = null
    private var spinnerItemGroup : Spinner? = null
    private var customDialog : Dialog? = null

    private var listItemGroupID = ArrayList<String>()
    private var listItemGroupOutPref = ArrayList<String>()
    private var listItemGroupName = ArrayList<String>()

    private var itemGroupID : String? = null
    private var itemGroupOutPref : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_picking_mod_wave_list)

        initViews()
    }

    private fun initViews() {
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val titleMenu : TextView = findViewById(R.id.submenu_title)
        titleMenu.text = session.getString("sub_menu_title", null)

        searchView = findViewById(R.id.pp1_searchview)
        searchView!!.setOnClickListener {
            search(searchView!!)
        }

        initCustomDialog()

        backBtnPressed()
        setupRecyclerView()
        swipeRefresh()
        search(searchView!!)
        spinnerItemGroup()
        retrieveData()
    }

    private fun initCustomDialog(){
        customDialog = Dialog(this@PickingModWaveListActivity)
        customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog!!.setContentView(R.layout.dialog_progress)
        customDialog!!.setCancelable(false)
    }

    private fun setupRecyclerView(){
        spinnerItemGroup = findViewById(R.id.spinner_item_group)
        recyclerView = findViewById(R.id.recycler_view_pp1)
        swipeRefreshLayout = findViewById(R.id.swl_data_pp1)
        progressBar = findViewById(R.id.pb_data)
        linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    private fun spinnerItemGroup(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingDataModWaveList> = res.pickingAllModWaveList(
            RequestPickingModWaveList(
                db_name = session.getString("db_name", null),
                task = "mod_wave_list",
                item_group_id = ""
            )
        )
        row.enqueue(object : Callback<ResponsePickingDataModWaveList>{
            override fun onResponse(
                call: Call<ResponsePickingDataModWaveList>,
                response: Response<ResponsePickingDataModWaveList>
            ) {
                itemgroup = response.body()?.item_group

                listItemGroupID.add("0")
                listItemGroupName.add("-- Item Group --")
                listItemGroupOutPref.add("")

                itemgroup?.forEach{
                    listItemGroupID.add(it.item_group_id.toString())
                    listItemGroupName.add(it.item_group_name.toString())
                    listItemGroupOutPref.add(it.outbound_pref_id.toString())
                }

                spinnerItemGroup!!.onItemSelectedListener = this@PickingModWaveListActivity
                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, listItemGroupName)
                spinnerItemGroup!!.adapter = adapter
            }

            override fun onFailure(call: Call<ResponsePickingDataModWaveList>, t: Throwable) {
                // do nothing
            }

        })
    }

    private fun retrieveData(){
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val res : RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )
        val row : Call<ResponsePickingDataModWaveList> = res.pickingAllModWaveList(
            RequestPickingModWaveList(
                db_name = session.getString("db_name", null),
                task = "mod_wave_list",
                item_group_id = itemGroupID
            )
        )
        row.enqueue(object : Callback<ResponsePickingDataModWaveList>{
            override fun onResponse(
                call: Call<ResponsePickingDataModWaveList>,
                response: Response<ResponsePickingDataModWaveList>
            ) {
                wavelist = response.body()?.wave_list
                adapterPickingAllModWaveList = AdapterPickingAllModWaveList(
                    applicationContext, wavelist!!, object : AdapterPickingAllModWaveList.OnAdapterListener{
                        override fun onClick(list: ModelsWaveList) {
                            customDialog!!.show()
                            session.edit()
                                .putString("item_group_id", itemGroupID)
                                .putString("outbound_pref_id", itemGroupOutPref)
                                .putString("wave_plan_id", list.wave_plan_id)
                                .putString("wave_comment", list.wave_comment)
                                .putString("no_so", list.no_so)
                                .putString("cust_name", list.cust_name)
                                .apply()
                            customDialog!!.dismiss()
                            startActivity(
                                Intent(applicationContext, PickingModInValLPNActivity::class.java)
                            )
                            //Toast.makeText(applicationContext, list.wave_plan_id, Toast.LENGTH_SHORT).show()
                        }
                    })
                adapterPickingAllModWaveList!!.setListItems(wavelist as MutableList<ModelsWaveList>)
                recyclerView?.adapter = adapterPickingAllModWaveList
                adapterPickingAllModWaveList!!.notifyDataSetChanged()
                customDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ResponsePickingDataModWaveList>, t: Throwable) {
                customDialog!!.dismiss()
                Toast.makeText(applicationContext, "Gagal menghubungi server!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun search(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterPickingAllModWaveList?.filter?.filter(newText)
                return true
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

    /*override fun onResume() {
        super.onResume()
        swipeRefresh()
        spinnerItemGroup()
        retrieveData()
    }*/

    private fun backBtnPressed(){
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        val backBtn : ConstraintLayout = findViewById(R.id.submenu_backicon)
        backBtn.setOnClickListener {
            customDialog!!.show()
            if(checkPageAccess.equals("true")){
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, HomeActivity::class.java)
                )
            }else{
                customDialog!!.dismiss()
                startActivity(
                    Intent(applicationContext, SubMenuActivity::class.java)
                )
            }
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialog!!.show()
        val session = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)
        val checkPageAccess = session.getString("fromBotNav", null)
        if(checkPageAccess.equals("true")){
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, HomeActivity::class.java)
            )
        }else{
            customDialog!!.dismiss()
            startActivity(
                Intent(applicationContext, SubMenuActivity::class.java)
            )
        }
        finish()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
        if(p0?.selectedItem == spinnerItemGroup?.selectedItem){
            if (p0?.selectedItem != "-- Item Group --"){
                itemGroupID = listItemGroupID[p2]
                itemGroupOutPref = listItemGroupOutPref[p2]
                retrieveData()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Do Nothing
    }

}