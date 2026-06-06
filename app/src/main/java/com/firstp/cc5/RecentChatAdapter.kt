package com.firstp.cc5

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firstp.cc5.models.ChatRoomModel
import com.google.firebase.database.FirebaseDatabase

class RecentChatAdapter(options: FirebaseRecyclerOptions<ChatRoomModel>,private val userNameMap : Map<String, String>,private val chatRoomIds : List<String>) :
    FirebaseRecyclerAdapter<ChatRoomModel, RecentChatAdapter.RecentChatViewHolder>(options) {

    inner class RecentChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val lastMessage = itemView.findViewById<TextView>(R.id.lastText)
        val time = itemView.findViewById<TextView>(R.id.time)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentChatAdapter.RecentChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_user_row, parent, false)
        return RecentChatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecentChatAdapter.RecentChatViewHolder,
        position: Int,
        model: ChatRoomModel
    ) {

        //we will need to use

        if (!chatRoomIds.contains(model.chatRoomId)){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
            return
        }
        else{
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val currentUserId = Utils.getUserid()
        val otherUserId = model.userIds.find { it != currentUserId }

        val userName = userNameMap[otherUserId]?: "Unkown User"
        holder.userName.text = userName




        FirebaseDatabase.getInstance().getReference("ChatRoom").child(model.chatRoomId).child("messages").limitToLast(1)
            .get().addOnSuccessListener { dataSnapshot ->

                var isMessageByMe = model.lastMessageSenderId.equals(Utils.getUserid())

                if(dataSnapshot.exists()){
                    val message = dataSnapshot.children.first().child("message").value as? String ?: ""
                    if(isMessageByMe){
                        holder.lastMessage.text = "You: $message"
                    }
                    else{
                        holder.lastMessage.text = message
                    }

                }
                //Can remove this for extra User of Say Hii
                else{
                    holder.lastMessage.text = "Say hii!!"
                }
            }

        holder.itemView.setOnClickListener {
            //navigate to the chat activity
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userName", userName)
            intent.putExtra("userId", otherUserId)

            holder.itemView.context.startActivity(intent)

        }

        val timeInMillis = model.lastMessageTimestamp
        val date = java.text.SimpleDateFormat("hh:mm a",java.util.Locale.getDefault())
        holder.time.text = date.format(java.util.Date(timeInMillis))
    }
}
