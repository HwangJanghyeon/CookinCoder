package com.example.ccp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UpdateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        setupToolbar()
    }
}