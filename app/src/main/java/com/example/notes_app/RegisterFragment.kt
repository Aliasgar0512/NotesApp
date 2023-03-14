package com.example.notes_app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notes_app.databinding.FragmentRegisterBinding
import com.example.notes_app.models.User
import com.example.notes_app.models.UserRequest
import com.example.notes_app.utils.NetworkResult
import com.example.notes_app.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // kotlin extension( viewModels<AuthViewModel>()) in fragments for creating ViewModel object which
    // behind the scenes is calling ViewModelProvider
    // for creating object of given ViewModel class

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val validationUser = validateUserInput()
            if (validationUser.first) {
                val userRequest = getUserRequest()
                authViewModel.registerUser(userRequest)

            } else {
                binding.txtError.text = validationUser.second
            }
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString()
        return UserRequest(emailAddress, password, username)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateUser(
            userRequest.email, userRequest.password, userRequest.username, false
        )
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.errorMessage
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}