package com.firstp.cc5

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firstp.cc5.databinding.ActivitySearchUserBinding
import com.firstp.cc5.models.Users
import com.google.firebase.database.FirebaseDatabase

class SearchUserActivity : AppCompatActivity() {



    private lateinit var binding: ActivitySearchUserBinding
    private lateinit var adapter: SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding= ActivitySearchUserBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setupRecyclerView()

        binding.searchButton.setOnClickListener {
            val searchText = binding.etSearch.text.toString()

            if(searchText.isEmpty()){
                binding.etSearch.error="Please enter a name"
            }
            else{
                adapter.stopListening()
                val query= FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").orderByChild("userName").startAt(searchText).endAt(searchText+"\uf8ff")
                val options = FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users::class.java).build()
                adapter.updateOptions(options)
                adapter.startListening()

            }
        }
    }

    private fun setupRecyclerView() {
        val query= FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").orderByChild("userName").equalTo("dummy")
        val options = FirebaseRecyclerOptions.Builder<Users>().setQuery(query, Users::class.java).build()
        adapter = SearchUserAdapter(options)
        binding.userList.layoutManager= LinearLayoutManager(this)
        binding.userList.adapter=adapter
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
}