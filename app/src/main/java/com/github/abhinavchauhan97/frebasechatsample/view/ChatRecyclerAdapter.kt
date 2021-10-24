package com.github.abhinavchauhan97.frebasechatsample.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.github.abhinavchauhan97.frebasechatsample.R
import com.github.abhinavchauhan97.frebasechatsample.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class ChatRecyclerAdapter : ListAdapter<ChatMessage, ChatRecyclerAdapter.MessageViewHolder>(
    diffCallback
) {

   inner class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView = view.findViewById<TextView>(R.id.name)
        val messageTextView = view.findViewById<TextView>(R.id.message)
        val contentContiner = view.findViewById<ConstraintLayout>(R.id.content_container)
        fun bind(message: ChatMessage) {
            contentContiner.backgroundTintList = ColorStateList.valueOf(message.color)
            nameTextView.text = message.name
            messageTextView.text = message.message
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if(item.userId == FirebaseAuth.getInstance().currentUser!!.uid) R.layout.chat_bubble_left else R.layout.chat_bubble_right
    }
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
                oldItem == newItem
        }
    }
}