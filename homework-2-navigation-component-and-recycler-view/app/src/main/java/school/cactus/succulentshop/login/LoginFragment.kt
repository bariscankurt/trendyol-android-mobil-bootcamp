package school.cactus.succulentshop.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.validation.EmailValidator
import school.cactus.succulentshop.validation.PasswordValidator

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val emailValidator = EmailValidator()

    private val passwordValidator = PasswordValidator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            logInButton.setOnClickListener{
                if(passwordInputLayout.isValid() and emailInputLayout.isValid()){
                    findNavController().navigate(R.id.action_loginFragment_to_productListFragment)
                }
            }
            createAccountButton.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    private fun TextInputLayout.isValid(): Boolean {
        val errorMessage = validator().validateLogin(editText!!.text.toString())
        error = errorMessage?.resolveAsString()
        isErrorEnabled = errorMessage != null
        return errorMessage == null
    }

    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when (this) {
        binding.emailInputLayout -> emailValidator
        binding.passwordInputLayout -> passwordValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}