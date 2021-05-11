package school.cactus.succulentshop.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentSignUpBinding
import school.cactus.succulentshop.validation.EmailValidator
import school.cactus.succulentshop.validation.PasswordValidator
import school.cactus.succulentshop.validation.UsernameValidator
import java.lang.IllegalArgumentException

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()
    private val usernameValidator = UsernameValidator()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        binding.apply {
            signUpButton.setOnClickListener {
                if (emailInputLayout.isValid() and passwordInputLayout.isValid() and usernameInputLayout.isValid()){
                    findNavController().navigate(R.id.action_signUpFragment_to_productListFragment)
                }
            }
            logInButton.setOnClickListener{
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }

        }
    }

    private fun TextInputLayout.isValid() : Boolean{
        val errorMessage = this.validator().validateSingIn(this.editText!!.text.toString())
        this.error = errorMessage?.resolveAsString()
        this.isErrorEnabled = errorMessage != null
        return errorMessage == null
    }

    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when(this){
        binding.emailInputLayout -> emailValidator
        binding.passwordInputLayout -> passwordValidator
        binding.usernameInputLayout -> usernameValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }
}