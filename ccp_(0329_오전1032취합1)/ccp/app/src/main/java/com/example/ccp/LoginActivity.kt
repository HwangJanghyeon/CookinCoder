package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccp.databinding.ActivityLoginBinding
import com.example.ccp.model.LoginRequest
import com.example.ccp.model.LoginResponse
import com.example.ccp.model.User
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

            // 0329 유효성 검사 추가
            // 사용자 입력 유효성 검사
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(username, password)
            loginUser(loginRequest)
            Log.d("로그인","${username}, ${password}")

        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        userService.login(loginRequest)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("loginResponse",loginResponse.toString())
                    if (loginResponse != null) {
                        // 로그인이 성공한 경우 사용자 정보를 가져와서 사용할 수 있음
                        val user = loginResponse.user
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        // 로그인이 성공하면 MainActivity로 이동하고, 로그인 상태와 사용자 정보를 전달
                        Log.d("로그인 성공시 유저 정보","$user")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        val intentToDetailActivity=Intent(this@LoginActivity, DetailActivity::class.java)
                        intent.putExtra("isLoggedIn", true)
                        intent.putExtra("user", user) // 사용자 데이터를 인텐트에 추가
                        intentToDetailActivity.putExtra("user",user) // 사용자 데이터를 디테일 액티비티로
                        startActivity(intent)
                        finish() // 로그인 액티비티 종료
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
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
