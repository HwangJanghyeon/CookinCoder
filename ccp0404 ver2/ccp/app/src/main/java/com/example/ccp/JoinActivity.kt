package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ccp.databinding.ActivityJoinBinding
import com.example.ccp.model.User
import com.example.ccp.model.UserResponse
import com.example.ccp.service.UserService
import com.example.ccp.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class JoinActivity : AppCompatActivity() {

    private lateinit var userService: UserService
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UserService 초기화
        userService = RetrofitClient.apiService

        // 회원가입 버튼 클릭 이벤트 처리
        binding.btnJoin.setOnClickListener {
            val username = binding.etIDJoin.text.toString()
            val name = binding.etNameJoin.text.toString()

            val password = binding.etPWJoin.text.toString()
            val email = binding.etEmailJoin.text.toString()

            // 0329 - 유효성 검사 추가
            // 사용자 입력 유효성 검사 - 아이디
            if (username.isEmpty()) {
                Toast.makeText(this@JoinActivity, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
                binding.etIDJoin.requestFocus() // 아이디 입력란으로 포커스 이동
                return@setOnClickListener
            }

            // 사용자 입력 유효성 검사 - 이름
            if (name.isEmpty()) {
                Toast.makeText(this@JoinActivity, "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                binding.etNameJoin.requestFocus() // 이름 입력란으로 포커스 이동
                return@setOnClickListener
            }

            // 사용자 입력 유효성 검사 - 이메일
            if (email.isEmpty()) {
                Toast.makeText(this@JoinActivity, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
                binding.etEmailJoin.requestFocus() // 이메일 입력란으로 포커스 이동
                return@setOnClickListener
            }

            // 이메일 형식 유효성 검사
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this@JoinActivity, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                binding.etEmailJoin.requestFocus() // 이메일 입력란으로 포커스 이동
                return@setOnClickListener
            }

            // 사용자 입력 유효성 검사 - 비밀번호
            if (password.isEmpty()) {
                Toast.makeText(this@JoinActivity, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                binding.etPWJoin.requestFocus() // 비밀번호 입력란으로 포커스 이동
                return@setOnClickListener
            }


            Log.d("JoinActivity", "사용자가 입력한 회원 정보: username=$username, name=$name, password=$password, email=$email")

            val newUser = User(username = username, name = name, password = password, email = email)

            joinUser(newUser)


        }
    }

    private fun joinUser(newUser: User) {
        userService.join(newUser)?.enqueue(object : Callback<UserResponse?> {
            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                if (response.isSuccessful) {
                    // 회원가입 성공
                    Toast.makeText(this@JoinActivity, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()

                    // 메인 액티비티로 이동
                    val intent = Intent(this@JoinActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료하여 메인 화면으로 이동
                } else {
                    // 서버로부터 응답을 받지 못한 경우의 처리
                    val errorMessage = response.errorBody()?.string()
                    Log.e("JoinActivity", "onResponse: $errorMessage")
                    Toast.makeText(this@JoinActivity, "회원가입에 실패했습니다. 에러 메시지: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse?>, t: Throwable) {
                // 네트워크 오류 등의 경우의 처리
                Toast.makeText(applicationContext, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
