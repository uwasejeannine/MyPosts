package com.example.mypost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telecom.Call
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Response


class CommentsActivity : AppCompatActivity() {
    var postId = 0
    lateinit var rvComments: RecyclerView
    lateinit var tvPostTitle: TextView
    lateinit var tvPostBody: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        postId = intent.getIntExtra("POST_ID", 0)
        castViews()
        getPost()
        getComment()

    }

    fun castViews() {
        tvPostTitle = findViewById(R.id.tvPostTitle)
        tvPostBody = findViewById(R.id.tvPostBody)
        rvComments = findViewById(R.id.rvComments)

    }

    fun getPost() {
        if (postId == 0) {
            Toast.makeText(baseContext, "post not found", Toast.LENGTH_LONG).show()
            finish()
        }
        var apiClient = ApiClient.buildApiClient(ApiInterface::class.java)
        var request = apiClient.getPost(postId)
        request.enqueue(object : Callback<Post?>{
            override fun onResponse(call: retrofit2.Call<Post?>, response: Response<Post?>) {
                if (response.isSuccessful){
                    var post=response.body()
                    tvPostTitle.text=post?.title
                    tvPostBody.text=post?.body
                }
            }

            override fun onFailure(call: retrofit2.Call<Post?>, t: Throwable) {
              Toast.makeText(baseContext,t.message,Toast.LENGTH_LONG).show()
            }

            })
    }
    fun getComment(){
        val retrofit=ApiClient.buildApiClient(ApiInterface::class.java)
        var request=retrofit.getComment(postId)
        request.enqueue(object :Callback<List<Comment>> {
            override fun onResponse(
                call: retrofit2.Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                var comments=response.body()!!
                var commentsAdapter=CommentsAdapter(comments,baseContext)
                rvComments.layoutManager=LinearLayoutManager(baseContext)
                rvComments.adapter=commentsAdapter
            }

            override fun onFailure(call: retrofit2.Call<List<Comment>>, t: Throwable) {
                Toast.makeText(baseContext,t.message,Toast.LENGTH_LONG).show()
            }

        })

    }
}


