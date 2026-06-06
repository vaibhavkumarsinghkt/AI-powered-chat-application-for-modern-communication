package com.firstp.cc5

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firstp.cc5.databinding.FragmentProfileBinding
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private var userId = Utils.getUserid()
    private var dbRef = FirebaseDatabase.getInstance().getReference("AllUsers/Users")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater)

        fetchUserDetails()
        binding.btnChangeName.setOnClickListener {
            binding.etHolder.visibility=View.VISIBLE
            binding.btnContinue.visibility=View.VISIBLE
            binding.btnChangeName.visibility=View.GONE
            binding.etUsername.requestFocus()

            binding.btnContinue.setOnClickListener {
                val newName =  binding.etUsername.text.toString()
                if(newName.isEmpty()){
                    binding.etUsername.error="Username field cannot be empty"
                }
                else{
                    dbRef.child(userId).child("userName").setValue(newName)
                    binding.etHolder.visibility=View.INVISIBLE
                    binding.btnContinue.visibility=View.GONE
                    binding.btnChangeName.visibility=View.VISIBLE
                    binding.userName.text = newName
                    Toast.makeText(context,"Name changed successfully",Toast.LENGTH_SHORT).show()
                }

            }

        }

        binding.logout.setOnClickListener {
            Utils.getFirebaseAuthInstance().signOut()
            startActivity(Intent(context, AuthActivity::class.java))
            activity?.finishAffinity()
        }

        return binding.root
    }

    private fun fetchUserDetails() {
        dbRef.child(userId).get().addOnSuccessListener { snapshot ->


            if (snapshot.exists()){

                val userName = snapshot.child("userName").value
                val phoneNumber = snapshot.child("phoneNumber").value

                binding.userName.text = userName.toString()
                binding.phNumber.text = phoneNumber.toString()

            }

        }.addOnFailureListener {
            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }



}