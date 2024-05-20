package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.databinding.ActivityDetailBinding
import com.example.ccp.util.hideKeyboard

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private lateinit var detailTitleEditText: EditText
    private lateinit var detailWriterEditText: EditText
    private lateinit var detailContentEditText: EditText
    private lateinit var detailIngrLists: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailTitleEditText = findViewById(R.id.detailTitle)
        detailWriterEditText = findViewById(R.id.detailWriter) // Initialize detailWriterEditText
        detailContentEditText = findViewById(R.id.detailContent)
        detailIngrLists = findViewById(R.id.ingrLists)

        // Intent에서 DTO 객체의 정보를 받아옴
        val boardTitle = intent.getStringExtra("board_title")
        val boardWriter = intent.getStringExtra("board_writer")
        val boardContent = intent.getStringExtra("board_content")

        // 받아온 정보를 EditText에 설정
        detailTitleEditText.setText(boardTitle)
        detailWriterEditText.setText(boardWriter)
        detailContentEditText.setText(boardContent)

        // 수정페이지로 이동하기
        binding.btnGoUpdate.setOnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
            // 필요하다면 업데이트에 필요한 데이터를 추가할 수 있습니다.
            startActivity(intent)
        }

        // 뒤로가기
        binding.btnBack.setOnClickListener { finish() }

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
