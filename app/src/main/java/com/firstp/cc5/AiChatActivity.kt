package com.firstp.cc5

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.firstp.cc5.databinding.ActivityAiChatBinding
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch

class AiChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityAiChatBinding
    // Start by instantiating a GenerativeModel and specifying the model name:
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            // This closes the current activity and returns to the previous one
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.sendBtn.setOnClickListener {
            val query = binding.editQuery.text.toString()
            binding.editQuery.setText("")
            if (query.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                lifecycleScope.launch {
                    val response = model.generateContent(query+  "(Under 100 words)")
                    binding.responseText.text = response.text
                    binding.progressBar.visibility = View.GONE
                }

            } else{
                binding.editQuery.error="Please enter a query before clicking the send button"
            }



        }


    }
}