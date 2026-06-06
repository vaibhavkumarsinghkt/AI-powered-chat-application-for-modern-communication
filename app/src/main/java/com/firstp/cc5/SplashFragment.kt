package com.firstp.cc5

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firstp.cc5.databinding.FragmentSplashBinding
import kotlinx.coroutines.launch
import java.util.logging.Handler


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel : AuthViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentSplashBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewModel.isCurrentUser.collect {
                    if (it){
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finishAffinity()
                    }
                    else{
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }

        },2500)
        return binding.root
    }


}