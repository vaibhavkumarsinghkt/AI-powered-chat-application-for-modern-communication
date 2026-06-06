package com.firstp.cc5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firstp.cc5.databinding.FragmentOTPBinding
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {
    lateinit var binding: FragmentOTPBinding
    private val viewModel : AuthViewModel by viewModels()

    lateinit var number: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentOTPBinding.inflate(layoutInflater)
       getUserNumber()
        Toast.makeText(requireContext(),"Sending OTP...",Toast.LENGTH_SHORT).show()
        sendOTP()
        onLoginButtonClicked()
        onBackPressed()
        return binding.root

    }

    private fun onBackPressed() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_loginFragment)
        }
    }

    private fun onLoginButtonClicked() {
        binding.btnContinue.setOnClickListener{
            val otp=binding.etOtp.text.toString()
            if(otp.length!=6){
                Toast.makeText(requireContext(),"Enter valid OTP",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"Verifying OTP...",Toast.LENGTH_SHORT).show()
                verifyOTP(otp)

            }


        }

    }

    private fun verifyOTP(otp: String) {
        viewModel.apply {
            signInWithPhoneAuthCredential(otp,requireActivity())
            lifecycleScope.launch {
                isSignedIn.collect{
                    if(it){
                        Toast.makeText(requireContext(),"Login Successful",Toast.LENGTH_SHORT).show()
                        //Navigate to other screen
                        val bundle= Bundle()
                        bundle.putString("number",number)
                        findNavController().navigate(R.id.action_OTPFragment_to_userDetail,bundle)
                    }else{
                        Toast.makeText(requireContext(),"Login Failed",Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }


    }

    private fun sendOTP() {
        viewModel.apply {
            sendOTP(number,requireActivity())
            lifecycleScope.launch {
                otpSent.collect {
                    if(it){
                        Toast.makeText(requireContext(),"OTP sent",Toast.LENGTH_SHORT).show()
                    }


                }
            }


        }



    }

    private fun getUserNumber() {
        val bundle=arguments
        if(bundle!=null){
            number=bundle.getString("number").toString()
            binding.number.text="+91 $number"
        }
    }


}