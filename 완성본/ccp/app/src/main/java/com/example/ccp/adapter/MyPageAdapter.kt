package com.example.ccp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.databinding.ItemMyPageContentBinding
import com.example.ccp.model.BoardDTO

class MyPageAdapter(private val myPosts: List<BoardDTO>) : RecyclerView.Adapter<MyPageAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemMyPageContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: BoardDTO) {
            binding.tvTitle.text = board.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyPageContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(myPosts[position])
    }

    override fun getItemCount(): Int = myPosts.size
}
