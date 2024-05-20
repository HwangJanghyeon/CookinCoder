package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.adapter.IngrBoardAdapter
import com.example.ccp.databinding.ActivityDetailBinding
import com.example.ccp.databinding.ItemIngredientBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.model.IngrBoard
import com.example.ccp.model.User
import com.example.ccp.service.ApiService
import com.example.ccp.util.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path


class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var bindingII: ItemIngredientBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        bindingII = ItemIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitClient.apiService

        // Intent로 전달된 번호(num) 가져오기
        val num = intent.getIntExtra("board_id", -1)
        if (num != -1) {
            loadData(num)
            loadIngredients(num)
            loadTotalPrice(num)
        }

        // 수정페이지로 이동하기
        binding.btnGoUpdate.setOnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
            // 필요하다면 업데이트에 필요한 데이터를 추가할 수 있습니다.
            startActivity(intent)
        }

        // 뒤로가기
        binding.btnBack.setOnClickListener { finish() }

        // 미보유 스위치 리스너 설정

    }

    // 서버로부터 게시글 데이터 불러오기
    private fun loadData(num: Int) {
        apiService.getBoardByNum(num)?.enqueue(object : Callback<BoardDTO?> {
            override fun onResponse(call: Call<BoardDTO?>, response: Response<BoardDTO?>) {
                val board = response.body()
                if (board != null) {
                    val title = board.title
                    val user = board.writer
                    val content = board.content

                    // UI 업데이트
                    updateUI(title, user, content)
                } else {
                    Log.e("DetailActivity", "Failed to load board details")
                }
            }

            override fun onFailure(call: Call<BoardDTO?>, t: Throwable) {
                Log.e("DetailActivity", "Failed to load board details: ${t.message}")
            }
        })
    }
    // 불러온 게시글 데이터를 모바일 화면에 출력
    private fun updateUI(title: String?, user: User?, content: String?) {
        // 받아온 게시물 정보를 UI에 반영합니다.
        binding.detailTitle.text = Editable.Factory.getInstance().newEditable(title)
        binding.detailWriter.text =
            Editable.Factory.getInstance().newEditable(user?.name ?: "Unknown")
        binding.detailContent.text = Editable.Factory.getInstance().newEditable(content)
    }

    // 서버로부터 입력받은 재료 목록 불러오기
    private fun loadIngredients(num: Int) {
        apiService.getIngredientsForBoard(num).enqueue(object : Callback<List<IngrBoard>> {
            override fun onResponse(
                call: Call<List<IngrBoard>>,
                response: Response<List<IngrBoard>>
            ) {
                val ingrBoards = response.body()
                // 재료 목록이 비어있는지 확인하고 UI에 표시
                if (ingrBoards != null && ingrBoards.isNotEmpty()) {
                    // IngrBoard 객체의 리스트를 반복문으로 순회합니다.
                    for (ingrBoard in ingrBoards) {
                        val name = ingrBoard.name
                        val unit = ingrBoard.unit
                        // 이름과 단위에 접근하여 필요한 작업을 수행합니다. 예를 들어, 로그에 출력하거나 다른 작업을 수행할 수 있습니다.
                        Log.d("DetailActivityLists", "재료 이름: $name, 단위: $unit")
                        // UI에 재료를 표시합니다.
                        displayIngredients(ingrBoards)
                    }
                } else {
                    // 재료 목록이 비어있을 때 처리
                    Log.e("DetailActivity", "Ingredient list is empty")
                }
            }

            override fun onFailure(call: Call<List<IngrBoard>>, t: Throwable) {
                Log.e("DetailActivity", "Failed to load ingredients: ${t.message}")
            }
        })
    }
    // 재료 목록을 RecyclerView에 표시
    private fun displayIngredients(ingrBoards: List<IngrBoard>) {
        // RecyclerView에 연결할 어댑터 생성
        val adapter = IngrBoardAdapter(this, ingrBoards)
        // RecyclerView에 어댑터 설정
        binding.recyclerViewIngredients.adapter = adapter
        // RecyclerView의 LayoutManager 설정
        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(this)
    }

    // 서버로부터 재료 총 가격 데이터 불러오기
    private fun loadTotalPrice(num: Int) {
        apiService.getTotalPrice(num).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val totalPrice = response.body()
                    Log.d("TotalPrice","${totalPrice}")
                    // 성공적으로 응답을 받았을 때의 처리
                } else {
                    // 서버로부터 응답을 받지 못했을 때의 처리
                    Log.e("TotalPrice", "TotalPrice is empty")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                // 통신 실패 시의 처리
                Log.e("TotalPrice", "Failed to load TotalPrice: ${t.message}")
            }
        })
    }


    // 화면 터치 시 키보드 숨기기
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            // rawX, rawY는 스크린, 즉 화면의 좌표값, x, y는 View안의 좌표값
            MotionEvent.ACTION_DOWN -> Log.d(
                ">>",
                "Touch down x: ${event.x} , rawX: ${event.rawX}"
            )

            MotionEvent.ACTION_UP -> Log.d(">>", "Touch up")
        }

        return super.onTouchEvent(event)
    }
}

