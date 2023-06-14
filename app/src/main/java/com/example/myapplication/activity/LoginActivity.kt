package com.example.myapplication.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseUserLogin
import com.example.myapplication.models.RequestModelsUsers
import com.example.myapplication.models.ResponseModelsUsers
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var ailoxdata: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        ailoxdata = getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

        val titleDB: TextView = findViewById(R.id.dbTitleLogin)
        titleDB.text = ailoxdata.getString("db_title", null).toString()
        val btnLogin: Button = findViewById(R.id.btn_login)
        val edUsername: TextInputEditText = findViewById(R.id.ed_loginUsername)
        val dbData: RequestApi = ApiServer().koneksiRetrofit().create(
            RequestApi::class.java
        )

        btnLogin.setOnClickListener {
            if(edUsername.text?.isEmpty() == true){
                edUsername.error = "Username is required!"
            }else{
                val userLogin: Call<ResponseUserLogin> = dbData.userLogin(
                    RequestModelsUsers(
                        db_name = ailoxdata.getString("db_name", null),
                        username = edUsername.text.toString(),
                        password = "transmarco"
                    )
                )
                userLogin.enqueue(object: Callback<ResponseUserLogin>{
                    override fun onResponse(
                        call: Call<ResponseUserLogin>,
                        response: Response<ResponseUserLogin>
                    ) {
                        val data: List<ResponseModelsUsers>? = response.body()?.data
                        if(data?.isEmpty() == true){
                            edUsername.error = "Username Salah!"
                        }else if(data?.get(0)?.is_login == "1"){
                            edUsername.error = "Account sedang di gunakan"
                        }else{
                            ailoxdata.edit()
                                .putString("userid", data?.get(0)?.user_id)
                                .putString("username", data?.get(0)?.user_name)
                                .putString("user_fullname", data?.get(0)?.user_fullname)
                                .putString("user_group_id", data?.get(0)?.user_group_id)
                                .putString("is_login", data?.get(0)?.is_login)
                                .apply()
                            startActivity(
                                Intent(applicationContext, HomeActivity::class.java)
                            )
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {
                        Toast.makeText(applicationContext, "Gagal Menghubungi Server!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(applicationContext, MainActivity::class.java)
        )
        finish()
    }
}