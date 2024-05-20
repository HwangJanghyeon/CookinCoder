package com.example.ccp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ccp.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 처음에는 MyPostsFragment를 표시
        replaceFragment(MyPostsFragment())

        binding.btnContent.setOnClickListener {
            // Content 버튼 클릭 시 MyPostsFragment로 교체
            replaceFragment(MyPostsFragment())
        }

        binding.btnHeart.setOnClickListener {
            // Heart 버튼 클릭 시 FavoritePostsFragment로 교체
            replaceFragment(FavoritePostsFragment())
        }

        binding.btnMoney.setOnClickListener {
            // Money 버튼 클릭 시 PaymentHistoryFragment로 교체
            replaceFragment(PaymentHistoryFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // FrameLayout에 프래그먼트를 교체
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) // 수정된 부분
            addToBackStack(null)
            commit()
        }
    }
}
