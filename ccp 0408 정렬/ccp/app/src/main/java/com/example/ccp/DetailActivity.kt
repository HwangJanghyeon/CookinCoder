package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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
import com.example.ccp.service.FavoriteRequest
import com.example.ccp.service.UpdatePriceRequest
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.RetrofitClient.commentService
import com.example.ccp.util.RetrofitClient.myPageService
import com.example.ccp.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var apiService: ApiService
    private var totalPrice: Int = 0
    private val boards = mutableListOf<IngrBoard>()
    private var num: Int = -1 // 보드 번호 추가


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        apiService = RetrofitClient.apiService

        val num = intent.getIntExtra("board_id", -1) // 보드 번호 초기화

        if (num != -1) {
            loadData(num)
            setupWebView(num) // 웹뷰 설정 메서드 호출
            loadComments(num)
        }

        // SharedPreferences 에서 로그인 정보 읽어오기
        val userId = SharedPreferencesHelper.getUserId(applicationContext)
        val username = SharedPreferencesHelper.getUsername(applicationContext)
        Log.d("상세보기에서 로그인 정보 불러오기", "사용자 ID: $userId")
        Log.d("상세보기에서 로그인 정보 불러오기", "사용자 이름: $username")
        // 결제요청 페이지로 이동하기
        binding.imageView1.setOnClickListener {
            val intent = Intent(this@DetailActivity, BuyActivity::class.java)
            startActivity(intent)
        }
        // 수정페이지로 이동하기
        binding.btnGoUpdate.setOnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
            intent.putExtra("board_id", num)
            startActivity(intent)
        }

        // 게시글 삭제
        binding.btnDeleteDatail.setOnClickListener {
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            // 댓글 삭제 함수 실행
            // 경고창 띄우기
            val alertDialogBuilder = AlertDialog.Builder(this@DetailActivity)
            alertDialogBuilder.setMessage("게시글을을 삭제하시겠습니까?")
            alertDialogBuilder.setPositiveButton("삭제") { _, _ ->
                // 확인 버튼을 누르면 삭제 함수 호출
                if (num != -1) {
                    deleteDetail(num)
                    // 게시글 삭제 후 메인 액티비티로 이동
                    startActivity(intent)
                }
            }
            alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
                // 취소 버튼을 누르면 아무런 동작도 하지 않음
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        // 뒤로가기
        binding.btnBack.setOnClickListener { finish() }

        // 댓글 관련 변수
        val addCommentButton: Button = findViewById(R.id.addComment)
        val inputComment: EditText = findViewById(R.id.inputComment)

        // 댓글 작성 버튼 클릭 이벤트 처리

        addCommentButton.setOnClickListener {
            val commentContent = inputComment.text.toString().trim()
            if (commentContent.isNotEmpty()) {
                // SharedPreferences에서 사용자 이름 가져오기
                val username = SharedPreferencesHelper.getUsername(this) ?: "Unknown"
                addCommentToServer(commentContent, num, username)
            }
            inputComment.text = null
        }


        binding.checkLike.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 찜하기를 체크한 경우
                val username = SharedPreferencesHelper.getUsername(applicationContext)
                if (username != null) {
                    val favoriteRequest = FavoriteRequest(username = username, boardId = num)
                    myPageService.addFavorite(favoriteRequest).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                // 성공적으로 서버에 찜 정보를 전송했을 때의 처리
                                Log.d("DetailActivity", "찜하기 정보가 성공적으로 전송되었습니다.")
                            } else {
                                // 서버 응답에 실패했을 때의 처리
                                Log.e("DetailActivity", "찜하기 정보 전송 실패: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // 네트워크 오류 등으로 호출 자체에 실패했을 때의 처리
                            Log.e("DetailActivity", "찜하기 정보 전송 실패: ${t.message}")
                        }
                    })
                } else {
                    // username이 null인 경우, 로그인이 되어있지 않다는 메시지 처리
                    Log.e("DetailActivity", "사용자 이름을 불러올 수 없습니다. 로그인이 필요합니다.")
                    // 여기서 사용자에게 로그인 화면으로 이동하라는 안내를 할 수도 있습니다.
                }
            } else {
                // 찜하기를 해제한 경우, 필요에 따라 처리
            }
        }

    }

    private fun loadData(num: Int) {
        apiService.getBoardByNum(num).enqueue(object : Callback<BoardDTO> {
            override fun onResponse(call: Call<BoardDTO>, response: Response<BoardDTO>) {
                if (response.isSuccessful) {
                    val board = response.body()
                    board?.let {
                        binding.detailTitle.text = Editable.Factory.getInstance().newEditable(it.title)
                        binding.detailWriter.text = Editable.Factory.getInstance().newEditable(it.writer?.name ?: "Unknown")
                        binding.detailContent.text = Editable.Factory.getInstance().newEditable(it.content)
                    }
                } else {
                    Log.e("DetailActivity", "게시글 정보를 불러오는데 실패했습니다. 응답코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BoardDTO>, t: Throwable) {
                Log.e("DetailActivity", "게시글 정보를 불러오는데 실패했습니다: ${t.message}", t)
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

    // 2024.04.05 작성글을 삭제
    private fun deleteDetail(num: Int) {
        // 게시글을 가져오는 요청
        val board: Call<BoardDTO> = apiService.getBoardByNum(num)
        board.enqueue(object : Callback<BoardDTO> {
            override fun onResponse(call: Call<BoardDTO>, response: Response<BoardDTO>) {
                if (response.isSuccessful) {
                    val boardData: BoardDTO? = response.body()
                    Log.d("게시글 삭제 과정", "$boardData")
                    // 게시글을 가져온 후 삭제 요청
                    if (boardData != null) {
                        val deleteCall: Call<Void> = apiService.deleteBoard(num, boardData.title)
                        deleteCall.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    // 게시글 삭제 성공
                                    Log.d("게시글 삭제", "게시글이 성공적으로 삭제되었습니다.")
                                } else {
                                    // 게시글 삭제 실패
                                    Log.e("게시글 삭제", "게시글 삭제에 실패했습니다.")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                // 통신 실패
                                Log.e("게시글 삭제", "통신 실패: ${t.message}")
                            }
                        })
                    } else {
                        // 가져온 게시글이 null인 경우
                        Log.e("게시글 삭제", "게시글이 존재하지 않습니다.")
                    }
                } else {
                    // 서버 응답이 실패인 경우
                    Log.e("게시글 삭제", "게시글 가져오기 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BoardDTO>, t: Throwable) {
                // 통신 실패
                Log.e("게시글 삭제", "통신 실패: ${t.message}")
            }
        })
    }

    private fun addCommentToServer(content: String, boardNum: Int, username: String) {
        // 댓글 작성 시 필요한 데이터 생성
        val commentDTO = CommentDTO(
            username = username,
            content = content,
            boardBnum = boardNum
        )

        // 서버로 댓글 추가 요청 보내기
        commentService.addComments(commentDTO, boardNum, username).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 성공적으로 댓글이 서버에 추가된 경우
                    Log.d("DetailActivity", "댓글이 성공적으로 추가되었습니다.")
                    loadComments(boardNum)
                } else {
                    // 서버로부터 실패 응답을 받은 경우
                    Log.e("DetailActivity", "댓글 추가 실패: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 통신 실패 시의 처리
                Log.e("DetailActivity", "댓글 추가 실패: ${t.message}")
            }
        })
    }

    private fun loadComments(boardNum: Int) {
        val currentUser = SharedPreferencesHelper.getUsername(applicationContext) // 현재 사용자 이름 가져오기

        commentService.getAllComments(boardNum).enqueue(object : Callback<List<CommentDTO>> {
            override fun onResponse(call: Call<List<CommentDTO>>, response: Response<List<CommentDTO>>) {
                val comments = response.body()
                if (comments != null) {
                    for (comment in comments) {
                        val writer = comment.username ?: "Unknown"
                        val time = comment.regdate.toString() // LocalDateTime을 String으로 변환
                        val content = comment.content
                        // 현재 사용자 이름과 함께 댓글 정보 로그에 출력
                        Log.d("comments", "CurrentUser: $currentUser, Writer: $writer, Content: $content, Time: $time")
                    }
                    displayComments(comments) // 댓글 표시 함수 호출
                } else {
                    Log.e("CommentList", "작성된 댓글이 없거나 댓글을 불러오지 못했습니다.")
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

    private fun setupWebView(num: Int) {
        val webView = binding.webviewDetail
        webView.settings.javaScriptEnabled = true // JavaScript 활성화
        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://10.100.103.73:8005/ingredient/$num") // 해당 URL 로드
    }
}
