package com.example.ccp

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.ccp.databinding.ActivityInsertBinding
import com.example.ccp.util.hideKeyboard

class InsertActivity : AppCompatActivity() {
    lateinit var binding: ActivityInsertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기
        binding.btnCancelInsert.setOnClickListener { finish() }
    }

    // 화면 터치 시 키보드 숨기기
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            // rawX, rawY는 스크린, 즉 화면의 좌표값, x, y는 View안의 좌표값
            MotionEvent.ACTION_DOWN -> Log.d(">>", "Touch down x: ${event.x} , rawX: ${event.rawX}")
            MotionEvent.ACTION_UP -> Log.d(">>", "Touch up")
        }
        hideKeyboard(this)
        return super.onTouchEvent(event)
    }
}