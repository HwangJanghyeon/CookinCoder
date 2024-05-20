package com.example.ccp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ccp.databinding.ActivityCompleteBinding

class CompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            val intent = Intent(this@CompleteActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "구매가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}