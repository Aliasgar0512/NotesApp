package com.example.notes_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notes_app.databinding.FragmentLoginBinding
import com.example.notes_app.models.UserRequest
import com.example.notes_app.utils.NetworkResult
import com.example.notes_app.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val validationUser = validateUserInput()
            if (validationUser.first) {
                val userRequest = getUserRequest()
                authViewModel.loginUser(userRequest)
            } else {
                binding.txtError.text = validationUser.second
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObserver()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress, password, "")
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateUser(
            userRequest.email, userRequest.password, userRequest.username, true
        )
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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