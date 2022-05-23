package yaroslavlebid.apps.myhome.ui.login.sign_in

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import yaroslavlebid.apps.myhome.R
import yaroslavlebid.apps.myhome.databinding.FragmentSignInBinding
import yaroslavlebid.apps.myhome.ui.home.HomeActivity
import yaroslavlebid.apps.myhome.ui.login.sign_up.RegistrationStatus
import yaroslavlebid.apps.myhome.ui.login.sign_up.RegistrationStatusMap
import yaroslavlebid.apps.myhome.ui.login.sign_up.SignUpFragment

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val signInViewModel: SignInViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignInBinding.bind(view)

        initListeners(binding)
        initObservers(binding)
    }

    private fun initListeners(binding: FragmentSignInBinding) {
        binding.signIn.setOnClickListener {
            signInViewModel.signIn(
                binding.email.editText?.text.toString(),
                binding.password.editText?.text.toString()
            )
        }
        binding.goToSignUp.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    private fun initObservers(binding: FragmentSignInBinding) {
        binding.run {
            signInViewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    loadingIndicator.visibility = View.VISIBLE
                    signIn.text = ""
                }
                else {
                    loadingIndicator.visibility = View.GONE
                    signIn.text = getString(R.string.sign_in_button_text)
                }
            }

            signInViewModel.loginStatus.observe(viewLifecycleOwner) { event ->
                if (event.status == LoginStatus.SUCCESS) {
                    val action = SignInFragmentDirections.actionSignInFragmentToHomeActivity()
                    findNavController().navigate(action)
                } else {
                    val errorMessage = when(event.status) {
                        LoginStatus.CUSTOM_ERROR -> event.customMessage
                        else -> event.status?.let {
                            LoginStatusMap.getErrorMessage(it)
                        }
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}