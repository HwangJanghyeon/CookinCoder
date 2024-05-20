package com.example.ccp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.databinding.FragmentFavoritePostsBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.service.MyPageService
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritePostsFragment : Fragment() {
    // View Binding을 위한 변수
    private var _binding: FragmentFavoritePostsBinding? = null
    private val binding get() = _binding!!

    // 어댑터 및 서비스 변수
    private lateinit var adapter: BoardAdapter
    private lateinit var myPageService: MyPageService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // View Binding 초기화
        _binding = FragmentFavoritePostsBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrofit 서비스 초기화
        myPageService = RetrofitClient.myPageService

        // 사용자가 찜한 게시물 로드
        loadFavoritePosts()

        return view
    }

    // 사용자가 찜한 게시물을 서버에서 로드하는 함수
    private fun loadFavoritePosts() {
        val userId = SharedPreferencesHelper.getUserId(requireContext())

        Log.d("FavoritePostsFragment", "Loading favorite posts for User ID: $userId")

        // 사용자 ID를 사용하여 찜한 게시물 가져오기
        myPageService.getUserFavorites(userId).enqueue(object : Callback<List<BoardDTO>> {
            override fun onResponse(call: Call<List<BoardDTO>>, response: Response<List<BoardDTO>>) {
                if (response.isSuccessful) {
                    val favoritePosts = response.body()
                    Log.d("FavoritePostsFragment", "Favorite posts loaded: ${favoritePosts?.size ?: "null"} posts")
                    favoritePosts?.let {
                        // 가져온 게시물을 RecyclerView에 표시
                        displayFavoritePosts(it)
                    }
                } else {
                    Log.e("FavoritePostsFragment", "Failed to load favorite posts. Response: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "찜한 게시물을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BoardDTO>>, t: Throwable) {
                Log.e("FavoritePostsFragment", "Network error occurred: ${t.message}")
                Toast.makeText(context, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // RecyclerView에 사용자가 찜한 게시물을 표시하는 함수
    private fun displayFavoritePosts(posts: List<BoardDTO>) {
        // BoardAdapter 초기화 및 설정
        if (!::adapter.isInitialized) {
            adapter = BoardAdapter(requireContext(), posts.toMutableList()) { num ->

            }
            binding.recyclerViewFavorite.layoutManager = GridLayoutManager(requireContext(), 2) // grid 2로 변경
            binding.recyclerViewFavorite.adapter = adapter
        } else {
            adapter.setData(posts) // 이 부분은 변경 없음
        }
    }

    // Fragment의 뷰가 소멸될 때 호출되는 함수
    override fun onDestroyView() {
        super.onDestroyView()
        // View Binding 해제
        _binding = null
    }
}

