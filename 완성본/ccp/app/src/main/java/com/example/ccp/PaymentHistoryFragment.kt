package com.example.ccp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ccp.adapter.BoardAdapter
import com.example.ccp.adapter.PaymentAdapter
import com.example.ccp.databinding.FragmentPaymentHistoryBinding
import com.example.ccp.model.BoardDTO
import com.example.ccp.model.PaymentRequest
import com.example.ccp.service.MyPageService
import com.example.ccp.util.RetrofitClient
import com.example.ccp.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentHistoryFragment : Fragment() {

    // View Binding을 위한 변수
    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!

    // 어댑터 및 서비스 변수
    private lateinit var adapter: PaymentAdapter
    private lateinit var myPageService: MyPageService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // View Binding 초기화
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrofit 서비스 초기화
        myPageService = RetrofitClient.myPageService

        // 사용자 게시물 로드
        loadUserPayment()

        // 뷰 리턴
        return view
    }


    // 결제내역 서버에서 로드하는 함수
    // SharedPreferences에서 로그인한 사용자의 username 가져오기
    private fun loadUserPayment() {
        val userId = SharedPreferencesHelper.getUserId(requireContext()) ?: return

        Log.d("PaymentHistoryFragment", "Logged in username: $userId")

        // 사용자의 결제내역 가져오기
        myPageService.getPaymentRequests(userId).enqueue(object : Callback<List<BoardDTO>> {
            override fun onResponse(call: Call<List<BoardDTO>>, response: Response<List<BoardDTO>>) {
                if (response.isSuccessful) {
                    val userPayment = response.body()
                    Log.d("FavoritePostsFragment", "Favorite posts loaded: ${userPayment?.size ?: "null"} posts")
                    userPayment?.let {
                        // 가져온 게시물을 recyclerView에 표시
                        displayUserPayment(it)
                    }
                } else {
                    // 게시물을 가져오는 데 실패한 경우 토스트 메시지 표시
                    Log.e("paymentPostsFragment", "Failed to load favorite posts. Response: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "결제내역을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BoardDTO>>, t: Throwable) {
                Log.e("paymentPostsFragment", "Network error occurred: ${t.message}")
                Toast.makeText(context, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // RecyclerView에 사용자 결제내역을 표시하는 함수
    private fun displayUserPayment(payments: List<BoardDTO>) {
        // PaymemntRequest 초기화 및 설정
        if (!::adapter.isInitialized) {
            adapter = PaymentAdapter(requireContext(), payments.toMutableList()) { num ->

            }

            binding.recyclerViewMyhistory.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewMyhistory.adapter = adapter
        } else {
            adapter.setData(payments)
        }
    }

    // Fragment의 뷰가 소멸될 때 호출되는 함수
    override fun onDestroyView() {
        super.onDestroyView()
        // View Binding 해제
        _binding = null
    }
}