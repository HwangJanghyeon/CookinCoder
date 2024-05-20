package com.example.ccp.adapter

import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ccp.R
import com.example.ccp.model.CommentDTO
import com.example.ccp.util.RetrofitClient.apiService
import com.example.ccp.util.RetrofitClient.commentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(
    private val context: Context,
    private var commentList: List<CommentDTO> // 변수명 변경
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position] // 변수명 변경
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return commentList.size // 변수명 변경
    }

    inner class ViewHolder(commentView: View) : RecyclerView.ViewHolder(commentView) {
        private val commentWriterTextView: TextView = commentView.findViewById(R.id.commentWriter)
        private val commentContentTextView: TextView = commentView.findViewById(R.id.commentContent)
        private val commentTimeTextView: TextView = commentView.findViewById(R.id.commentTime)
        private val linkDeleteComment: TextView = itemView.findViewById(R.id.linkDeleteComment)
        private val linkUpdateComment: TextView = itemView.findViewById(R.id.linkUpdateComment)
        private val linkCancelEdit: TextView = commentView.findViewById(R.id.linkCancelEdit)
        private val textView: TextView = commentView.findViewById(R.id.commentContent)
        private val editText: EditText = commentView.findViewById(R.id.editComment)

        fun bind(comment: CommentDTO) {
            commentWriterTextView.text = "작성자명: ${comment.username ?: "알 수 없음"}" // 수정된 부분
            Log.d("comment.username", "${comment.username}") // 수정된 부분
            Log.d("commentWriterTextView","${commentWriterTextView.text}")
            commentContentTextView.text = comment.content
            Log.d("commentContentTextView","${commentContentTextView.text}")
            commentTimeTextView.text = comment.regdate.toString()

            // 댓글 삭제 버튼 클릭 이벤트 처리
            linkDeleteComment.setOnClickListener {
                Log.d("deleteComment", "댓글 삭제 버튼")
                // 경고창 띄우기
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setMessage("댓글을 삭제하시겠습니까?")
                alertDialogBuilder.setPositiveButton("삭제") { _, _ ->
                    // 확인 버튼을 누르면 삭제 함수 호출
                    deleteCommentToServer(comment.cnum)
                }
                alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
                    // 취소 버튼을 누르면 아무런 동작도 하지 않음
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }

            // 댓글 수정 버튼 클릭 이벤트 처리
            linkUpdateComment.setOnClickListener {
                Log.d("updateComment", "댓글 수정 버튼")
                // 수정 버튼 누르기
                if (textView.visibility == View.VISIBLE) {
                    // 기존 댓글 표시 숨기기, 삭제 버튼 숨기기
                    textView.visibility = View.GONE
                    linkDeleteComment.visibility = View.GONE
                    // 댓글 편집창 보이기, 수정 취소 버튼 보이기
                    editText.visibility = View.VISIBLE
                    linkCancelEdit.visibility = View.VISIBLE
                    // 기존 댓글 편집창에서도 보이게 하기
                    val updatedContent = comment.content ?: ""
                    editText.text = Editable.Factory.getInstance().newEditable(updatedContent)
                }
                // 수정 버튼 한번 더 누르기
                else {
                    // 댓글 수정 함수
                    updateCommentToServer(comment.cnum)
                    // 기존 댓글 표시 보이기, 삭제 버튼 보이기
                    textView.visibility = View.VISIBLE
                    linkDeleteComment.visibility = View.VISIBLE
                    // 댓글 편집창 숨기기, 수정 취소 버튼 숨기기
                    editText.visibility = View.GONE
                    linkCancelEdit.visibility = View.GONE
                    // 편집창 텍스트 초기화
                    editText.text = null

                }
            }
            // 댓글 수정 취소 버튼을 누름
            linkCancelEdit.setOnClickListener {
                // 기존 댓글 표시 보이기, 삭제 버튼 보이기
                textView.visibility = View.VISIBLE
                linkDeleteComment.visibility = View.VISIBLE
                // 댓글 편집창 숨기기, 수정 취소 버튼 숨기기
                editText.visibility = View.GONE
                linkCancelEdit.visibility = View.GONE
                // 편집창 텍스트 초기화
                editText.text = null
                updateCommentToServer(comment.cnum)
            }
        }

        // 댓글 삭제 함수
        fun deleteCommentToServer(cnum: Long?) {
            cnum?.let { cnumNotNull ->
                commentService.deleteComments(cnumNotNull.toInt()).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            // 서버에서 성공적으로 삭제됐을 때 로컬 데이터 갱신
                            val newList = commentList.toMutableList()
                            newList.removeIf { it.cnum == cnumNotNull }
                            updateData(newList)
                        } else {
                            Toast.makeText(context, "댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("CommentAdapter", "댓글 삭제 실패", t)
                        Toast.makeText(context, "네트워크 오류로 댓글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // 댓글 수정 함수
        private fun updateCommentToServer(cnum: Long?) {
            val updatedContent = editText.text.toString().trim() // EditText에서 사용자가 입력한 수정된 댓글 내용 가져오기

            if (updatedContent.isNotEmpty()) {
                cnum?.let { cnumNotNull ->
                    val updatedComment = CommentDTO(content = updatedContent) // 수정된 댓글 내용을 포함한 CommentDTO 객체 생성
                    commentService.updateComments(cnumNotNull.toInt(), updatedComment).enqueue(object : Callback<CommentDTO> {
                        override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                            if (response.isSuccessful) {
                                val updatedCommentResponse = response.body()
                                if (updatedCommentResponse != null) {
                                    // 서버에서 수정된 댓글 정보를 받아온 경우
                                    // 해당 댓글을 RecyclerView에서 갱신
                                    val newList = commentList.toMutableList()
                                    val index = newList.indexOfFirst { it.cnum == cnumNotNull }
                                    if (index != -1) {
                                        newList[index] = updatedCommentResponse
                                        updateData(newList)
                                    }
                                }
                                Toast.makeText(context, "댓글이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                            Log.e("CommentAdapter", "댓글 수정 실패", t)
                            Toast.makeText(context, "네트워크 오류로 댓글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(context, "댓글 내용 수정이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터 변경 시 호출하여 RecyclerView 갱신
    private fun updateData(newCommentList: List<CommentDTO>) {
        commentList = newCommentList
        notifyDataSetChanged()
    }

}