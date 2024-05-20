package com.example.ccp.adapter

import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.databinding.ItemIngredientBinding
import com.example.ccp.model.IngrBoard
import com.example.ccp.service.UpdatePriceRequest

class IngrBoardAdapter(
    private val context: Context,
    private var boards: MutableList<IngrBoard>,
    private val onItemStateChanged: (String, Boolean) -> Unit,
    private val updateTotalPrice: (Int, UpdatePriceRequest) -> Unit,
    private val boardNum: Int
) : RecyclerView.Adapter<IngrBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boards[position], position)
    }

    override fun getItemCount(): Int = boards.size

    inner class ViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(board: IngrBoard, position: Int) {
            binding.ingrName.text = Editable.Factory.getInstance().newEditable(board.name)
            binding.ingrUnit.text = Editable.Factory.getInstance().newEditable(board.unit.toString())
            binding.ingrGet.isChecked = board.isOwned

            // 상태 변경 시 로그
            Log.d("IngrBoardAdapter", "Switch changed for ${board.name} at position $position to ${binding.ingrGet.isChecked}")

            binding.ingrGet.setOnCheckedChangeListener(null)
            binding.ingrGet.setOnCheckedChangeListener { _, isChecked ->
                // 모델 업데이트 로그
                Log.d("IngrBoardAdapter", "Updated model for ${board.name} at position $position to $isChecked")

                // 가격 업데이트 로직 호출 전 로그
                Log.d("IngrBoardAdapter", "Updating price for ${board.name} with isOwned = $isChecked")
                boards[position].isOwned = isChecked
                onItemStateChanged(board.name ?: "", isChecked)
                updateTotalPrice(boardNum, UpdatePriceRequest(board.name ?: "", isChecked))

            }
        }
    }

}
