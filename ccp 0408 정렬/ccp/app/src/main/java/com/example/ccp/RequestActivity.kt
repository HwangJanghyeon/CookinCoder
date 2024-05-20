package com.example.ccp


import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.ccp.databinding.ActivityRequestBinding

class RequestActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestBinding
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupWebView()
    }

    override fun onBackPressed() {
        if (::webView.isInitialized && webView.url == "http://10.100.103.73:8005/request/request_list") {
            super.onBackPressed() // 웹뷰에서 뒤로가기 동작
        } else {
            binding.requestWebview.goBack() // 액티비티에서 뒤로가기 동작
        }
    }

    private fun setupWebView() {
        val webView = binding.requestWebview
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // 자바스크립트 사용 허용
        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://10.100.103.73:8005/request/request_list")
    }
}