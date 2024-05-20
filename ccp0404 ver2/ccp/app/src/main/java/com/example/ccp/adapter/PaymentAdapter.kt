package com.example.ccp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.databinding.ItemPaymentBinding
import com.example.ccp.model.BoardDTO

class PaymentAdapter(private val context: Context,
                     private var boards: MutableList<BoardDTO>,
                     private val itemClickListener: OnItemClickListener? = null)
    : RecyclerView.Adapter<PaymentAdapter.MyPaymentViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(num: Int)
    }

    inner class MyPaymentViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvHistoryTitle.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(boards[position].num)
                }
            }
            binding.tvDone.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(boards[position].num)
                }
            }
        }

        fun bind(board: BoardDTO) {
            binding.tvHistoryTitle.text =  board.title
            binding.tvDone.text
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPaymentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPaymentBinding.inflate(layoutInflater, parent, false)
        return MyPaymentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    override fun onBindViewHolder(holder: MyPaymentViewHolder, position: Int) {
        val board = boards[position]
        holder.bind(board)
    }

    fun setData(newBoards: List<BoardDTO>) {
        boards.clear()
        boards.addAll(newBoards)
        notifyDataSetChanged()
    }
}