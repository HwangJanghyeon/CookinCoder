package com.example.ccp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccp.databinding.ActivityLoginBinding
import com.example.ccp.model.LoginRequest
import com.example.ccp.service.LoginResponse

import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = binding.etIDLogin.text.toString()
        val password = binding.etPWLogin.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "로그인 시도: $username")

        // Retrofit을 사용하여 서버에 로그인 요청
        val userService = RetrofitClient.apiService
        userService.login(LoginRequest(username, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    Log.d(TAG, "로그인 성공: ${loginResponse.message}")


                    SharedPreferencesHelper.saveLoginInfo(applicationContext, loginResponse.username, loginResponse.token)
                    SharedPreferencesHelper.saveUsername(applicationContext, loginResponse.username) // 사용자의 username을 SharedPreferences에 저장
                    Log.d(TAG, "로그인 유저 네임: ${loginResponse.username}")
                    Toast.makeText(applicationContext, "로그인 성공!", Toast.LENGTH_SHORT).show()

                    // 메인 액티비티로 이동
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish() // LoginActivity 종료
                } else {
                    Log.w(TAG, "로그인 실패: ${response.errorBody()?.string()}")
                    Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "네트워크 오류", t)
                Toast.makeText(applicationContext, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
