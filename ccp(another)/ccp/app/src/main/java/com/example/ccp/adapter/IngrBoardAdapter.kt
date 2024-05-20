package com.example.ccp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.model.IngrBoard
import com.example.ccp.R

class IngrBoardAdapter(
    private val context: Context,
    private var boards: List<IngrBoard>
) : RecyclerView.Adapter<IngrBoardAdapter.ViewHolder>() {

    // 생성자 수정
    constructor(context: Context) : this(context, emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = boards[position]
        holder.bind(board)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingrNameTextView: TextView = itemView.findViewById(R.id.ingrName)
        private val ingrUnitTextView: TextView = itemView.findViewById(R.id.ingrUnit)
        private val ingrGetSwitch: Switch = itemView.findViewById(R.id.ingrGet)

        fun bind(board: IngrBoard) {
            ingrNameTextView.text = board.name
            ingrUnitTextView.text = board.unit.toString()
            // Switch 설정
            // ingrGetSwitch.isChecked = board.isOwned
        }
    }
}
