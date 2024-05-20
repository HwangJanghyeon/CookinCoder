package com.example.ccp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.adapter.MyPageAdapter
import com.example.ccp.databinding.FragmentMyPostsBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.service.ApiService
import com.example.ccp.service.MyPageService
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostsFragment : Fragment() {
    //View Binding 초기화: onCreateView 메서드 내에서 FragmentMyPostsBinding을 사용하여 프래그먼트의 레이아웃을 초기화
    //Retrofit 서비스 초기화: Retrofit을 사용하여 서버에서 데이터를 가져오기 위한 서비스를 초기화
    //데이터 로드: 서버에서 사용자 게시물을 가져오는 함수인 loadUserPosts()를 호출
    //데이터 표시: 서버에서 가져온 게시물을 RecyclerView에 표시하기 위한 함수인 displayUserPosts()를 호출합 이때 RecyclerView에 어댑터를 설정
    //어댑터 설정: RecyclerView에 표시될 사용자 게시물을 처리하기 위한 어댑터를 초기화하고 설정
    //View Binding 해제: Fragment의 뷰가 소멸될 때 View Binding을 해제하여 메모리 누수를 방지
    // View Binding을 위한 변수
    private var _binding: FragmentMyPostsBinding? = null
    private val binding get() = _binding!!

    // 어댑터 및 서비스 변수
    private lateinit var adapter: BoardAdapter
    private lateinit var myPageService: MyPageService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // View Binding 초기화
        _binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrofit 서비스 초기화
        myPageService = RetrofitClient.myPageService

        // 사용자 게시물 로드
        loadUserPosts()

        return view
    }

    // 사용자 게시물을 서버에서 로드하는 함수
    private fun loadUserPosts() {
        // SharedPreferences에서 로그인한 사용자의 username 가져오기
        val username = SharedPreferencesHelper.getUsername(requireContext()) ?: return

        Log.d("MyPostsFragment", "Logged in username: $username")

        // 사용자의 username을 사용하여 게시물 가져오기
        myPageService.getUserPostsByUsername(username).enqueue(object : Callback<List<BoardDTO>> {
            override fun onResponse(call: Call<List<BoardDTO>>, response: Response<List<BoardDTO>>) {
                if (response.isSuccessful) {
                    val userPosts = response.body()
                    userPosts?.let {
                        // 가져온 게시물을 RecyclerView에 표시
                        displayUserPosts(it)
                    }
                } else {
                    // 게시물을 가져오는 데 실패한 경우 토스트 메시지 표시
                    Toast.makeText(context, "게시물을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BoardDTO>>, t: Throwable) {
                // 네트워크 오류가 발생한 경우 토스트 메시지 표시
                Toast.makeText(context, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // RecyclerView에 사용자 게시물을 표시하는 함수
    private fun displayUserPosts(posts: List<BoardDTO>) {
        // BoardAdapter 초기화 및 설정
        if (!::adapter.isInitialized) {
            adapter = BoardAdapter(requireContext(), posts.toMutableList()) { num ->

            }
            binding.recyclerViewMyPosts.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewMyPosts.adapter = adapter
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

