package com.example.ccp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.databinding.ActivityMainBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.service.ApiService
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.hideKeyboard
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var boardAdapter: BoardAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // 설정 메뉴 아이템 클릭 시 실행할 코드
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewBoards.layoutManager = LinearLayoutManager(this)
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
}
