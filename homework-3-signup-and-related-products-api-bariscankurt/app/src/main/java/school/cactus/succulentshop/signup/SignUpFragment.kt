package school.cactus.succulentshop.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.signup.SignupRequest
import school.cactus.succulentshop.api.signup.SignupResponse
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentSignUpBinding
import school.cactus.succulentshop.signup.validation.SignupEmailValidator
import school.cactus.succulentshop.signup.validation.SignupPasswordValidator
import school.cactus.succulentshop.signup.validation.SignupUsernameValidator

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val emailValidator = SignupEmailValidator()
    private val passwordValidator = SignupPasswordValidator()
    private val usernameValidator = SignupUsernameValidator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            signUpButton.setOnClickListener {
                if (emailInputLayout.isValid() and passwordInputLayout.isValid() and usernameInputLayout.isValid()) {
                    view.hideKeyboard()
                    sendSignupRequest()

                }
            }

            logInButton.setOnClickListener {
                findNavController().navigate(R.id.signUpToLogin)
            }
        }
        requireActivity().title = getString(R.string.sign_up)
    }

    private fun sendSignupRequest() {
        val email = binding.emailInputLayout.editText!!.text.toString()
        val password = binding.passwordInputLayout.editText!!.text.toString()
        val username = binding.usernameInputLayout.editText!!.text.toString()

        val request = SignupRequest(email, password, username)
        api.register(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                when (response.code()) {
                    200 -> onSignupSuccess(response.body()!!)
                    in 400..499 -> onClientError(response.errorBody())
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Snackbar.make(
                    requireView(),
                    R.string.check_your_connection,
                    LENGTH_INDEFINITE
                )
                    .setAction("Retry") {
                        sendSignupRequest()
                    }
                    .show()
            }
        })
    }

    private fun onUnexpectedError() {
        Snackbar.make(requireView(), R.string.unexpected_error_occured, LENGTH_LONG).show()
    }

    private fun onClientError(errorBody: ResponseBody?) {
        if (errorBody == null) return onUnexpectedError()
        try {
            val message = errorBody.errorMessage()
            Snackbar.make(requireView(), message, LENGTH_LONG).show()
        } catch (ex: JsonSyntaxException) {
            onUnexpectedError()
        }
    }

    private fun ResponseBody.errorMessage(): String {
        var errorBody = this.string()
        val gson = GsonBuilder().create()
        val signupErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return signupErrorResponse.message[0].messages[0].message
    }

    private fun onSignupSuccess(response: SignupResponse) {
        JwtStore(requireContext()).saveJwt(response.jwt)
        findNavController().navigate(R.id.signUpToProductList)
    }

    private fun TextInputLayout.isValid(): Boolean {
        val errorMessage = this.validator().validate(this.editText!!.text.toString())
        this.error = errorMessage?.resolveAsString()
        this.isErrorEnabled = errorMessage != null
        return errorMessage == null
    }

    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when (this) {
        binding.emailInputLayout -> emailValidator
        binding.passwordInputLayout -> passwordValidator
        binding.usernameInputLayout -> usernameValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }

    private fun View.hideKeyboard() {
        // we are using this function to dismiss virtual keyboard when we press login or signup
        // because when we give the wrong credentials, we can't see the snackbar because
        // keyboard overlapping on it
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}