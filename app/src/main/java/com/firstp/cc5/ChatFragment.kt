package com.firstp.cc5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firstp.cc5.databinding.FragmentChatBinding
import com.firstp.cc5.models.ChatRoomModel
import com.firstp.cc5.models.Users
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : Fragment() {
   lateinit var binding: FragmentChatBinding
   lateinit var adapter: RecentChatAdapter
   private val userNameMap = HashMap<String, String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChatBinding.inflate(layoutInflater)


        fetchAllUserNames {
            setupSearchRecyclerView()
        }





        return binding.root

    }

    private fun fetchAllUserNames(onComplete: () -> Unit){
        FirebaseDatabase.getInstance().getReference("AllUsers/Users").get().addOnSuccessListener {
            dataSnapshot ->
            for (userSnapshot in dataSnapshot.children){
                val userId = userSnapshot.key ?: continue
                val userName = userSnapshot.child("userName").value as? String ?: "Unknown User"
                userNameMap[userId] = userName

            }
            onComplete()



        }

    }

    private fun setupSearchRecyclerView() {
       val userChatRoomRef = FirebaseDatabase.getInstance().getReference("UserChatRooms")
           .child(Utils.getUserid())
           .get().addOnSuccessListener { dataSnapshot ->
               val chatRoomIds = mutableListOf<String>()
               for (chatRoomSnapshot in dataSnapshot.children){

                   chatRoomIds.add(chatRoomSnapshot.key!!)
               }

               val query = FirebaseDatabase.getInstance().getReference("ChatRoom")
                   .orderByChild("lastMessageTimestamp")

               val options = FirebaseRecyclerOptions.Builder<ChatRoomModel>()
                   .setQuery(query, ChatRoomModel::class.java)
                   .setLifecycleOwner(this)
                   .build()

               adapter = RecentChatAdapter(options, userNameMap, chatRoomIds)
               binding.recentChats.layoutManager = LinearLayoutManager(context).apply {
                   reverseLayout = true
               }
               binding.recentChats.adapter = adapter
               adapter.startListening()



           }




    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized){
            adapter.startListening()
        }
    }

    override fun onStop(){
        super.onStop()
        if (::adapter.isInitialized){
            adapter.stopListening()
        }

    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized){
            adapter.notifyDataSetChanged()
        }

    }


}
