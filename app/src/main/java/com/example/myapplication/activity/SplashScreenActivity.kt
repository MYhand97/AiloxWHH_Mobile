package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.UserTimeout
import com.example.myapplication.data.api.ApiServer
import com.example.myapplication.data.api.request.RequestApi
import com.example.myapplication.data.api.response.ResponseUserLogin
import com.example.myapplication.models.RequestModelsUsers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if(getSharedPreferences("ailoxwms_data", MODE_PRIVATE).getString("username",null) != null){
                UserTimeout.lastUserActivityTimestamp = System.currentTimeMillis()
                checkTimeout()
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                finish()
            }else{
                UserTimeout.lastUserActivityTimestamp = System.currentTimeMillis()
                checkTimeout()
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }, 600)
    }

    override fun onResume() {
        super.onResume()
        UserTimeout.lastUserActivityTimestamp = System.currentTimeMillis()
        checkTimeout()
    }

    private fun checkTimeout() {
        val currentTime = System.currentTimeMillis()
        val lastTimestamp = UserTimeout.lastUserActivityTimestamp
        val timeoutThreshold: Long = 1 * 60 * 1000 // 1 Minutes in miliseconds
        if(currentTime - lastTimestamp > timeoutThreshold){
            clearUser()
        }
    }

    private fun clearUser() {
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
        userLogout.enqueue(object: Callback<ResponseUserLogin> {
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
                finishAffinity()
                finish()
            }
            override fun onFailure(call: Call<ResponseUserLogin>, t: Throwable) {

            }
        })
    }
}