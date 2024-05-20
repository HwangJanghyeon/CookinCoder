package com.example.ccp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccp.databinding.ActivityLoginBinding
import com.example.ccp.model.LoginRequest
import com.example.ccp.model.LoginResponse
import com.example.ccp.service.UserService
import com.example.ccp.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var userService: UserService
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UserService 초기화
        userService = RetrofitClient.apiService

        // 로그인 버튼 클릭 이벤트 처리
        binding.btnLogin.setOnClickListener {
            val username = binding.etIDLogin.text.toString()
            val password = binding.etPWLogin.text.toString()
            val loginRequest = LoginRequest(username, password)
            loginUser(loginRequest)
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        userService.login(loginRequest)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}