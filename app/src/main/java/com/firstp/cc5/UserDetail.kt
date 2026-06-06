package com.firstp.cc5

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firstp.cc5.databinding.FragmentUserDetailBinding
import com.firstp.cc5.models.Users
import com.google.firebase.database.FirebaseDatabase


class UserDetail : Fragment() {
    private lateinit var binding: FragmentUserDetailBinding
    var username:String=""
    var userNumber=""
    var userId=""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUserDetailBinding.inflate(layoutInflater)

        getDetails()

        return binding.root
    }

    private fun getDetails() {
        binding.btnContinue.setOnClickListener {
            username=binding.etUsername.text.toString()

            if(username.length==0){
                binding.etUsername.error ="User name cannot be empty"
            }
            else{
                val user= Users(userId, userNumber, username)
                FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(userId).setValue(user)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()

            }


        }
        val bundle=arguments
       userNumber=bundle!!.getString("number").toString()
        userId=Utils.getUserid()

    }


}