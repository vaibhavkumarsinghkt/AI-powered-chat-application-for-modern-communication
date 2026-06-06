package com.firstp.cc5

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firstp.cc5.models.MessageModel


class ChatAdapter(options: FirebaseRecyclerOptions<MessageModel>) :
    FirebaseRecyclerAdapter<MessageModel, ChatAdapter.ChatViewHolder>(options) {

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val leftHolder = itemView.findViewById<CardView>(R.id.leftHolder)
        val rightHolder = itemView.findViewById<CardView>(R.id.rightHolder)
        val leftMessage = itemView.findViewById<TextView>(R.id.leftChat)
        val rightMessage = itemView.findViewById<TextView>(R.id.rightChat)



    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatAdapter.ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_row, parent, false)
        return ChatViewHolder(view)

    }

    override fun onBindViewHolder(
        holder: ChatAdapter.ChatViewHolder,
        position: Int,
        model: MessageModel
    ) {

        if(model.senderId.equals(Utils.getUserid())){
            holder.leftHolder.visibility=View.GONE
            holder.rightHolder.visibility=View.VISIBLE
            holder.rightMessage.text=model.message

        }
        else{
            holder.rightHolder.visibility=View.GONE
            holder.leftHolder.visibility=View.VISIBLE
            holder.leftMessage.text=model.message

        }


    }


}