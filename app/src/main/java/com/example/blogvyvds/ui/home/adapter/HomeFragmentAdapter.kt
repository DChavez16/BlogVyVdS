package com.example.blogvyvds.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogvyvds.core.BaseViewHolder
import com.example.blogvyvds.core.hide
import com.example.blogvyvds.core.show
import com.example.blogvyvds.data.model.Post
import com.example.blogvyvds.databinding.PostItemBinding

class HomeFragmentAdapter(private val postList: List<Post>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder) {
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    override fun getItemCount(): Int = postList.size

    private inner class HomeScreenViewHolder(
        val binding: PostItemBinding,
        val context: Context
    ): BaseViewHolder<Post>(binding.root) {
        override fun bind(item: Post) {
            Glide.with(context).load(item.userImg).centerCrop().into(binding.imgPostUserPic)
            binding.txtPostUserName.text = item.userName
            binding.txtPostDate.text = item.date
            binding.txtPostHour.text = item.time

            binding.txtPostDescription.text = item.description

            item.imageUrl?.let {
                if(it.isNotEmpty()) Glide.with(context).load(it).centerCrop().into(binding.imgPostImage)
            }
            item.fileUrl?.let {
                if(it.isNotEmpty()) {
                    binding.txtPostFileName.text = item.fileName
                    binding.btnPostDescargarArchivo.show()
                }
            }
        }
    }
}