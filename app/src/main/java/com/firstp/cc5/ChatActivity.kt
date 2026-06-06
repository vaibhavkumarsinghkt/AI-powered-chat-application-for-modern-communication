package com.firstp.cc5

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firstp.cc5.databinding.ActivityChatBinding
import com.firstp.cc5.models.ChatRoomModel
import com.firstp.cc5.models.MessageModel
import com.firstp.cc5.models.Users
import com.google.firebase.Timestamp
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    var chatRoomId : String = ""
    var user2Id: String=""

    var adapter : ChatAdapter?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        createGetChatRoom()
        setupMessageAdapter()
        binding.sendBtn.setOnClickListener {
            var message : String = binding.message.text.toString()
            if (message.isEmpty()){
                binding.message.error = "Please enter a message before sending"
            }
            else{
                sendMessage(message)

            }
        }

    }

    private fun setupMessageAdapter() {
        val query= FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").child("messages").orderByChild("timestamp/seconds")
        val options = FirebaseRecyclerOptions.Builder<MessageModel>().setQuery(query, MessageModel::class.java).build()
        adapter = ChatAdapter(options)
        binding.messageList.layoutManager= LinearLayoutManager(this).apply {
            stackFromEnd=true
        }
        binding.messageList.adapter=adapter
        adapter?.startListening()
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.messageList.scrollToPosition(adapter!!.itemCount -1)
            }
        })

    }

    private fun sendMessage(message: String) {
//        val chatRoom = ChatRoomModel(lastMessageTimestamp = Timestamp.now(), lastMessageSenderId = Utils.getUserid())
//
//        FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").setValue(chatRoom)
//
//        val messageModel : MessageModel = MessageModel(message, Utils.getUserid(), Timestamp.now())
//
//        FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").child("messages").push().setValue(messageModel)
//
//        binding.message.setText("")

        val chatRoomRef = FirebaseDatabase.getInstance().getReference("ChatRoom").child(chatRoomId)

        chatRoomRef.child("lastMessageTimestamp").setValue(System.currentTimeMillis())
        chatRoomRef.child("lastMessageSenderId").setValue(Utils.getUserid())

        val messageModel = MessageModel(message, Utils.getUserid(), System.currentTimeMillis())
        chatRoomRef.child("messages").push().setValue(messageModel)
        binding.message.setText("")



    }


    fun setupToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val userName = intent.getStringExtra("userName")
        binding.toolbar.title = userName
    }

    fun createGetChatRoom(){
        user2Id=intent.getStringExtra("userId").toString()
        chatRoomId = getChatRoomId(Utils.getUserid(),user2Id)
        FirebaseDatabase.getInstance().getReference("ChatRoom").child(chatRoomId).get().addOnSuccessListener {snapshot ->
            if (snapshot.exists()){
                //chatroom exists

            }
            else{
                //chat room dosent exists
                val userIds = ArrayList<String>()
                userIds.add(Utils.getUserid())
                userIds.add(user2Id)
                val chatRoom = ChatRoomModel(chatRoomId, userIds, System.currentTimeMillis(),"")

                FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").setValue(chatRoom)

            }

            //Map chatroom to both users and save it in UserChatRoomList

            val userChatRoomRef = FirebaseDatabase.getInstance().getReference("UserChatRooms")
            userChatRoomRef.child(Utils.getUserid()).child(chatRoomId).setValue(true)
            userChatRoomRef.child(user2Id).child(chatRoomId).setValue(true)





        }.addOnFailureListener {




        }


    }

    private fun getChatRoomId(u1 : String , u2 : String) : String{
        return if(u1.hashCode() < u2.hashCode()){
            u1 + "_" + u2
        }else{
            u2 + "_" + u1
        }

    }
}