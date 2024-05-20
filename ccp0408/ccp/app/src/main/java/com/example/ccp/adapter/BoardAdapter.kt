package com.example.ccp.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.databinding.ItemBoardBinding
import com.example.ccp.databinding.ItemBoardListBinding
import com.example.ccp.model.BoardDTO
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale


class BoardAdapter(
    private val context: Context,
    private var boards: MutableList<BoardDTO>,
    private val itemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isGridLayout = true
    fun interface OnItemClickListener {
        fun onItemClick(num: Int)
    }
    companion object {
        private const val TYPE_GRID = 0
        private const val TYPE_LIST = 1
    }

    inner class BoardListViewHolder(private val binding: ItemBoardListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(boards[position].num)
                }
            }
        }

        fun bind(board: BoardDTO) {
            binding.tvTitle.text = board.title

            // 작성자 이름과 날짜 형식을 적절히 설정
            binding.tvAuthor.text = board.username
            // SimpleDateFormat을 사용하여 날짜를 문자열로 포맷팅
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            binding.tvRegdate.text = dateFormat.format(board.regdate)
            // 이미지 URL에 타임스탬프 추가
            val imageUrl = board.imageUrl?.let {
                "http://10.100.103.73:8005$it?timestamp=${System.currentTimeMillis()}" // 서버 주소와 이미지 경로 수정, 타임스탬프 추가
            }

            if (!imageUrl.isNullOrEmpty()) {
                Log.d("PicassoLoading", "Image load start: $imageUrl") // 로딩 시작 로그 추가

                Picasso.get()
                    .load(imageUrl)
                    .into(binding.imageView, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            // 이미지 로딩 성공 로그
                            Log.d("PicassoLoading", "Image load success")
                        }

                        override fun onError(e: Exception?) {
                            // 이미지 로딩 실패 로그
                            Log.e("PicassoLoading", "Image load failed: ${e?.message}")
                        }
                    })
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (isGridLayout) TYPE_GRID else TYPE_LIST
    }

    inner class BoardViewHolder(private val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(boards[position].num)
                }
            }
        }

        fun bind(board: BoardDTO) {
            binding.tvTitle.text = board.title

            // 이미지 URL에 타임스탬프 추가
            val imageUrl = board.imageUrl?.let {
                "http://10.100.103.73:8005$it?timestamp=${System.currentTimeMillis()}" // 서버 주소와 이미지 경로 수정, 타임스탬프 추가
            }

            if (!imageUrl.isNullOrEmpty()) {
                Log.d("PicassoLoading", "Image load start: $imageUrl") // 로딩 시작 로그 추가

                Picasso.get()
                    .load(imageUrl)
                    .into(binding.imageView, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            // 이미지 로딩 성공 로그
                            Log.d("PicassoLoading", "Image load success")
                        }

                        override fun onError(e: Exception?) {
                            // 이미지 로딩 실패 로그
                            Log.e("PicassoLoading", "Image load failed: ${e?.message}")
                        }
                    })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_GRID -> {
                val binding = ItemBoardBinding.inflate(inflater, parent, false)
                BoardViewHolder(binding)
            }
            TYPE_LIST -> {
                val binding = ItemBoardListBinding.inflate(inflater, parent, false) // 가정한 바인딩 클래스
                BoardListViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val board = boards[position]
        when (holder) {
            is BoardViewHolder -> holder.bind(board)
            is BoardListViewHolder -> holder.bind(board)
            else -> throw IllegalArgumentException("Unknown ViewHolder type")
        }
    }


    override fun getItemCount(): Int {
        return boards.size
    }

    fun setData(newBoards: List<BoardDTO>) {
        boards.clear()
        boards.addAll(newBoards)
        notifyDataSetChanged()
    }

    fun setBoardImage(board: BoardDTO, bitmap: Bitmap) {
        val position = boards.indexOf(board)
        if (position != -1) {
            notifyItemChanged(position)
        }
    }
}
