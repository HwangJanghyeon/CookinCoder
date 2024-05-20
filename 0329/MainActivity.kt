package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.databinding.ActivityMainBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.service.ApiService
import com.example.ccp.util.RetrofitClient
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var apiService: ApiService

    // 0329 로그인 후 로그아웃 버튼 변경 설정
    var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 0329 로그인 후 로그아웃 버튼 변경 설정
        // 이전에 저장된 로그인 상태를 가져와서 적용
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Toast.makeText(this@MainActivity, "FAB Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 액티비티 이동
        binding.appBarMain.signupButton.setOnClickListener {
            val intent = Intent(this@MainActivity, JoinActivity::class.java)
            startActivity(intent)
        }

        // 0329 로그인 후 로그아웃 버튼 변경 설정
        // 로그인 버튼 클릭 리스너 설정
        binding.appBarMain.loginButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // 0329 로그인 후 로그아웃 버튼 변경 설정
        // 로그아웃 버튼 클릭 리스너 설정
        binding.appBarMain.logoutButton.setOnClickListener {
            // 로그아웃 처리
            isLoggedIn = false
            // 갱신된 로그인 상태에 따라 버튼 표시 변경
            updateButtonVisibility()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initRecyclerView()
        apiService = RetrofitClient.apiService
        loadBoards()

        // 검색 버튼 클릭 리스너 설정
        binding.btnSearch.setOnClickListener {
            val searchQuery = binding.editTextSearch.text.toString()
            searchBoards(searchQuery)
        }

        // 0329 로그인 후 로그아웃 버튼 변경 설정
        // 앱이 최초로 시작될 때 로그인 상태에 따라 버튼 표시 변경
        updateButtonVisibility()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // 0329
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.MyPage_set -> {
                // MyPage 설정 메뉴 아이템 클릭 시 실행할 코드
                true
            }
            R.id.Request_set -> {
                // Request 설정 메뉴 아이템 클릭 시 실행할 코드
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewBoards.layoutManager = GridLayoutManager(this, 2)
        boardAdapter = BoardAdapter(this, emptyList(), object : BoardAdapter.OnItemClickListener {
            override fun onItemClick(num: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("board_id", num)
                startActivity(intent)
            }
        })
        binding.recyclerViewBoards.adapter = boardAdapter
    }

    private fun loadBoards() {
        apiService.getAllBoards()?.enqueue(object : Callback<List<BoardDTO>?> {
            override fun onResponse(
                call: Call<List<BoardDTO>?>,
                response: Response<List<BoardDTO>?>
            ) {
                val boards = response.body() ?: emptyList()
                Log.d("MainActivity", "Boards: $boards")
                boardAdapter.setData(boards)
            }

            override fun onFailure(call: Call<List<BoardDTO>?>, t: Throwable) {
                Log.e("MainActivity", "Failed to load boards: ${t.message}")
            }
        })
    }

    private fun searchBoards(query: String) {
        // editTextSearch null 체크 추가
        binding.editTextSearch?.let { editText ->
            val searchQuery = editText.text.toString()
            apiService.searchBoards(searchQuery)?.enqueue(object : Callback<List<BoardDTO>?> {
                override fun onResponse(
                    call: Call<List<BoardDTO>?>,
                    response: Response<List<BoardDTO>?>
                ) {
                    val boards = response.body() ?: emptyList()
                    Log.d("MainActivity", "Search results: $boards")
                    boardAdapter.setData(boards)
                }

                override fun onFailure(call: Call<List<BoardDTO>?>, t: Throwable) {
                    Log.e("MainActivity", "Failed to search boards: ${t.message}")
                }
            })
        } ?: run {
            // editTextSearch가 null인 경우 처리
            Log.e("MainActivity", "EditTextSearch is null")
        }
    }

    // 0329 로그인 후 로그아웃 버튼 변경 설정
    // 로그인 상태에 따라 버튼 표시 변경 함수
    private fun updateButtonVisibility() {
        if (isLoggedIn) {
            // 로그인 상태일 때
            binding.appBarMain.loginButton.visibility = View.GONE
            binding.appBarMain.logoutButton.visibility = View.VISIBLE
        } else {
            // 로그아웃 상태일 때
            binding.appBarMain.loginButton.visibility = View.VISIBLE
            binding.appBarMain.logoutButton.visibility = View.GONE
        }
    }
}
