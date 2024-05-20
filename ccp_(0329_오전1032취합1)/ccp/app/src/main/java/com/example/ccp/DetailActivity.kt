package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.CommentAdapter
import com.example.ccp.adapter.IngrBoardAdapter
import com.example.ccp.databinding.ActivityDetailBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.model.CommentDTO
import com.example.ccp.model.IngrBoard
import com.example.ccp.model.User
import com.example.ccp.service.ApiService
import com.example.ccp.service.CommentService
import com.example.ccp.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private lateinit var apiService: ApiService
    private lateinit var commentService: CommentService
    private var num: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = RetrofitClient.apiService
        commentService = RetrofitClient.commentService

        // Intent로 전달된 번호(num) 가져오기
        val num = intent.getIntExtra("board_id", -1)
        if (num != -1) {
            loadData(num)
            setupWebView(num)
            Log.d("웹뷰", "${setupWebView(num)}")
            loadComments(num)
            // 재료 목록 관련
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

        // 댓글 관련 변수
        val addCommentButton: Button = findViewById(R.id.addComment)
        val inputComment: EditText = findViewById(R.id.inputComment)
        // Intent에서 사용자 정보 받기
        val user = intent.getParcelableExtra<User>("user")
        Log.d("로그인된 유저 정보 확인", "Received user data: $user")

        // 댓글 작성 버튼 클릭 이벤트 처리
        addCommentButton.setOnClickListener {
            val commentContent = inputComment.text.toString().trim()
            if (commentContent.isNotEmpty()) {
                // 댓글 내용이 비어 있지 않은 경우에만 서버로 전송
                if (user != null) {
                    addCommentToServer(commentContent, num, user)
                }
            }
            inputComment.text = null
        }

    }

    private fun setupWebView(num: Int) {
        val webView = binding.webviewDetail
        webView.settings.javaScriptEnabled = true // JavaScript 활성화
        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://10.100.103:42:8005/ingredient/$num") // 해당 URL 로드
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
                    Log.d("AndroidBoardNum", "${board.num}")

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

    // 서버로 댓글 추가 요청을 보내는 함수
    private fun addCommentToServer(commentContent: String, boardNum: Int, user: User) {
        // 게시글 번호를 사용하여 게시글 정보를 가져오기
        val board = apiService.getBoardByNum(boardNum)
        board?.enqueue(object : Callback<BoardDTO?> {
            override fun onResponse(call: Call<BoardDTO?>, response: Response<BoardDTO?>) {
                val boardData = response.body()
                if (boardData != null) {
                    // 댓글 작성 시 필요한 데이터 생성 (예: 작성자 이름, 내용)
                    val commentDTO = CommentDTO(
                        writerUsername = user?.username ?: "댓글_작성자", // 사용자명이 없을 경우 기본값으로 설정
                        content = commentContent,
                        boardBnum = boardData.num
                    )
                    // 서버로 댓글 추가 요청 보내기
                    commentService.addComments(commentDTO, boardNum, user)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    // 성공적으로 댓글이 서버에 추가된 경우
                                    // 필요한 작업 수행 (예: 성공 메시지 표시, 화면 갱신 등)
                                    Log.d("comment", commentDTO.toString())
                                    Log.d("DetailActivity", "댓글이 성공적으로 추가되었습니다.")
                                    loadComments(boardNum)
                                    // 예시: 댓글 추가 후 화면을 갱신하거나 다른 작업 수행
                                } else {
                                    // 서버로부터 실패 응답을 받은 경우
                                    // 오류 처리 (예: 실패 메시지 표시)
                                    Log.e("DetailActivity", "댓글 추가 실패: ${response.message()}")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                // 통신 실패 시의 처리
                                // 오류 처리 (예: 네트워크 오류 메시지 표시)
                                Log.e("DetailActivity", "댓글 추가 실패: ${t.message}")
                            }
                        })
                } else {
                    Log.e("DetailActivity", "Failed to load board data")
                }
            }

            override fun onFailure(call: Call<BoardDTO?>, t: Throwable) {
                Log.e("DetailActivity", "Failed to load board data: ${t.message}")
            }
        })
    }

    // 댓글창 불러오기
    private fun loadComments(boardNum: Int) {
        commentService.getAllComments(boardNum).enqueue(object : Callback<List<CommentDTO>> {
            override fun onResponse(
                call: Call<List<CommentDTO>>,
                response: Response<List<CommentDTO>>
            ) {
                val comments = response.body()
                if (comments != null) {
                    for (comment in comments) {
                        val writer = comment.writerUsername
                        val time = comment.regdate.toString() // LocalDateTime을 String으로 변환
                        val content = comment.content
                        Log.d("comments", "$writer//$content//$time")
                        displayComments(comments)
                    }
                } else {
                    Log.e("CommentList", "Failed to load comments")
                }
            }

            override fun onFailure(call: Call<List<CommentDTO>>, t: Throwable) {
                Log.e("CommentList", "Failed to load comments: ${t.message}")
            }
        })
    }

    // 댓글창 불러오기
    private fun displayComments(comment: List<CommentDTO>) {
        // RecyclerView에 연결할 어댑터 생성
        val adapter = CommentAdapter(this, comment)
        // RecyclerView에 어댑터 설정
        binding.replyList.adapter = adapter
        // RecyclerView의 LayoutManager 설정
        binding.replyList.layoutManager = LinearLayoutManager(this)
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
                    Log.d("TotalPrice", "${totalPrice}")
                    displayTotalPrice(totalPrice)
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

    // 재료 총 가격 출력
    private fun displayTotalPrice(totalPrice: Int?) {
        if (totalPrice != null) {
            val totalPriceText = totalPrice.toString() // Int를 String으로 변환
            binding.totalPrice.text = Editable.Factory.getInstance().newEditable(totalPriceText)
        } else {
            // totalPrice가 null인 경우에 대한 처리
            Log.e("TotalPrice", "Total price is null")
        }
    }
}

