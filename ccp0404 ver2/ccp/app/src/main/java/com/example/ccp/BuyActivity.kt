package com.example.ccp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ccp.databinding.ActivityBuyBinding

class BuyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuy.setOnClickListener {
            val intent = Intent(this@BuyActivity, CompleteActivity::class.java)
            startActivity(intent)
        }
    }
}