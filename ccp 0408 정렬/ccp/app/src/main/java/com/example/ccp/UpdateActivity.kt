package com.example.ccp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import com.example.ccp.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val num = intent.getIntExtra("board_id", -1) // 보드 번호 초기화

        if (num != -1) {
            setupWebView(num)
        }
    }

    private fun setupWebView(num: Int) {
        val webView = binding.webviewUpdate
        webView.settings.javaScriptEnabled = true // JavaScript 활성화
        webView.webViewClient = WebViewClient()
        // JavaScript 인터페이스 설정
        webView.addJavascriptInterface(WebAppInterface(), "Android")
        webView.loadUrl("http://10.100.103.42:8005/update_for_mobile/$num") // 해당 URL 로드
    }

    inner class WebAppInterface{
        // JavaScript에서 호출할 함수 정의
        @JavascriptInterface
        fun redirectToMainActivity() {
            // MainActivity로 이동
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }
}