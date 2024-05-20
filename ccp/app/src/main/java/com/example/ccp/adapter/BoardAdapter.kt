package com.example.ccp.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.model.BoardDTO
import com.example.ccp.databinding.ItemBoardBinding
import com.squareup.picasso.Picasso

class BoardAdapter(private val context: Context, private var boards: List<BoardDTO>, private val itemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(num: Int)
    }

    inner class BoardViewHolder(private val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {

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


            val imageUrl = board.imageUrl?.let {
                "http://10.100.103.73:8005$it" // 서버 주소와 이미지 경로 수정
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = boards[position]
        holder.bind(board)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    fun setData(newBoards: List<BoardDTO>) {
        boards = newBoards
        notifyDataSetChanged()
    }

    fun setBoardImage(board: BoardDTO, bitmap: Bitmap) {
        val position = boards.indexOf(board)
        if (position != -1) {
            notifyItemChanged(position)
        }
    }
}
