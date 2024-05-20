package com.example.ccp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ccp.databinding.ActivityMyPageBinding


class MyPageActivity : BaseActivity() {

    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 처음에는 MyPostsFragment를 표시
        replaceFragment(MyPostsFragment())
        setupToolbar()
        binding.btnContent.setOnClickListener {
            // Content 버튼 클릭 시 MyPostsFragment로 교체
            replaceFragment(MyPostsFragment())
            Log.d("MyPageActivity", "Content 버튼 클릭됨")
        }

        binding.btnHeart.setOnClickListener {
            // Heart 버튼 클릭 시 FavoritePostsFragment로 교체
            replaceFragment(FavoritePostsFragment())
            Log.d("MyPageActivity", "Heart 버튼 클릭됨")
        }

        binding.btnMoney.setOnClickListener {
            // Money 버튼 클릭 시 PaymentHistoryFragment로 교체
            replaceFragment(PaymentHistoryFragment())
            Log.d("MyPageActivity", "Money 버튼 클릭됨")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // FrameLayout에 프래그먼트를 교체
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) // 수정된 부분
            addToBackStack(null)
            commit()
        }
        Log.d("MyPageActivity", "프래그먼트 교체됨: $fragment")
    }
}
