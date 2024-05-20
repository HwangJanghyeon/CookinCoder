package com.example.ccp



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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
import com.example.ccp.model.Category
import com.example.ccp.service.ApiService
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.RetrofitClient.ingrService
import com.example.ccp.util.SharedPreferencesHelper
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var apiService: ApiService
    private var categoryMap: Map<String, Long> = emptyMap()
    // 0329 로그인 후 로그아웃 버튼 변경 설정
    var isLoggedIn = false
    private val insertActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 데이터 추가 후 새로고침 로직
            loadBoards() // 게시판을 다시 로드하여 최신 데이터를 표시
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        apiService = RetrofitClient.apiService

        loadCategoryMap() // 카테고리 맵 로딩
        loadBoards() // 기본 게시판 로딩

        // 로그인 상태 확인 및 UI 업데이트
        updateLoginStatus()
        setupButtonListeners()







        binding.appBarMain.fab.setOnClickListener { view ->
            Toast.makeText(this@MainActivity, "레시피 등록 클릭", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 액티비티 이동
        binding.appBarMain.signupButton.setOnClickListener {
            val intent = Intent(this@MainActivity, JoinActivity::class.java)
            startActivity(intent)
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


        navView.setNavigationItemSelectedListener { menuItem ->
            Log.d("MainActivity", "Menu Item Clicked: ${menuItem.title}")
            when (menuItem.itemId) {
                R.id.nav_category1 -> {
                    // 예: 카테고리 이름이 "찜/찌개"인 경우
                    val categoryId = categoryMap["찜/찌개"]
                    categoryId?.let {
                        loadBoardsByCategory(it)

                    }
                    true
                }
                R.id.nav_category2 -> {
                    // 카테고리 이름이 "면류"인 경우
                    val categoryId = categoryMap["면류"]
                    categoryId?.let {
                        loadBoardsByCategory(it)
                    }
                    true
                }
                // 다른 카테고리에 대한 처리...
                else -> false
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


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
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)

                true
            }
            R.id.Request_set -> {
                // Request 설정 메뉴 아이템 클릭 시 실행할 코드
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
    override fun onResume() {
        super.onResume()
        // 로그인 상태가 변경되었는지 확인하고 UI 업데이트
        updateLoginStatus()
    }
    private fun setupButtonListeners() {
        // 로그인 버튼 클릭 리스너
        binding.appBarMain.loginButton.setOnClickListener {
            // 로그인 액티비티를 시작
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        // 로그아웃 버튼 클릭 리스너
        binding.appBarMain.logoutButton.setOnClickListener {
            // SharedPreferences를 사용하여 로그인 정보를 제거하고 로그아웃 처리
            SharedPreferencesHelper.clearLoginInfo(this)
            isLoggedIn = false
            updateButtonVisibility()
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            // 선택적으로 로그인 화면으로 이동하거나, 현재 액티비티를 새로고침 할 수 있습니다.
        }
    }

    // 0329 로그인 후 로그아웃 버튼 변경 설정
    // 로그인 상태에 따라 버튼 표시 변경 함수
    private fun updateButtonVisibility() {
        binding.appBarMain.loginButton.visibility = if (isLoggedIn) View.GONE else View.VISIBLE
        binding.appBarMain.logoutButton.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
    }

    private fun updateLoginStatus() {
        isLoggedIn = SharedPreferencesHelper.isLoggedIn(this)
        updateButtonVisibility()
    }
    private fun loadCategoryMap() {
        apiService.getAllCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        // 카테고리 이름과 ID를 매핑하여 categoryMap을 초기화
                        categoryMap = categories.associate { it.name to it.id }
                    }
                } else {
                    Log.e("MainActivity", "Failed to load categories")
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("MainActivity", "Error loading categories: ${t.message}")
            }
        })
    }

    private fun loadBoardsByCategory(categoryId: Long) {
        Log.d("MainActivity", "Loading boards for category ID: $categoryId")
        apiService.getBoardsByCategory(categoryId).enqueue(object : Callback<List<BoardDTO>> {
            override fun onResponse(
                call: Call<List<BoardDTO>>,
                response: Response<List<BoardDTO>>
            ) {
                if (response.isSuccessful) {
                    val boards = response.body() ?: emptyList()
                    boardAdapter.setData(boards)
                    Log.d("MainActivity", "Boards loaded successfully: $boards")
                } else {
                    Toast.makeText(this@MainActivity, "게시물을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("MainActivity", "Failed to load boards for category ID: $categoryId")
                }
            }

            override fun onFailure(call: Call<List<BoardDTO>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "카테고리 버튼 실패: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("MainActivity", "Failed to load boards for category ID: $categoryId", t)
            }
        })
    }


    private fun initRecyclerView() {
        binding.recyclerViewBoards.layoutManager = GridLayoutManager(this, 2)
        boardAdapter = BoardAdapter(this, mutableListOf(), object : BoardAdapter.OnItemClickListener {
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
            override fun onResponse(call: Call<List<BoardDTO>?>, response: Response<List<BoardDTO>?>) {
                runOnUiThread {
                    val boards = response.body() ?: emptyList()
                    Log.d("MainActivity", "Boards: $boards")
                    boardAdapter.setData(boards)
                }
            }

            override fun onFailure(call: Call<List<BoardDTO>?>, t: Throwable) {
                runOnUiThread {
                    Log.e("MainActivity", "Failed to load boards: ${t.message}")
                }
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
}