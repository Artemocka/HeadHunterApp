package com.dracul.feature_main

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dracul.common.CommonDrawables
import com.dracul.common.CommonString
import com.dracul.feature_main.databinding.FragmentSignInBinding
import com.dracul.feature_main.model.SignInActions
import com.dracul.feature_main.model.SignInEvents
import com.dracul.feature_main.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private val vm by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.tvHeader) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePaddingRelative(top = systemBars.top)
            insets
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.etLayout.error = if (state.isError) getString(CommonString.email_error) else  null
                if(state.email.isNotEmpty()) {
                    binding.etLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), CommonDrawables.ic_big_close)
                    binding.etLayout.setEndIconTintMode(PorterDuff.Mode.DST)
                    binding.etLayout.setErrorIconDrawable(CommonDrawables.ic_big_close)

                }
                else
                    binding.etLayout.endIconDrawable = null

            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            vm.events.collect { event ->
                when (event) {
                    SignInEvents.NextScreen -> {
                    }

                    SignInEvents.ClearEmailET -> {
                        binding.etEmail.setText("")
                    }
                }
            }
        }
        binding.btnContinue.setOnClickListener {
            vm.onAction(SignInActions.ClickContinueButton)
        }
        binding.etLayout.setErrorIconOnClickListener{
            vm.onAction(SignInActions.ClickClearButton)
        }
        binding.etEmail.addTextChangedListener {
            vm.onAction(SignInActions.EditEmailEditText(it.toString()))
        }
        return binding.root
    }


}