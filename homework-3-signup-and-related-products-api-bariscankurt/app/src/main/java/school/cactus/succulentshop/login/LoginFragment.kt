package school.cactus.succulentshop.login

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
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.login.validation.LoginIdentifierValidator
import school.cactus.succulentshop.login.validation.LoginPasswordValidator

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val identifierValidator = LoginIdentifierValidator()

    private val passwordValidator = LoginPasswordValidator()

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
            logInButton.setOnClickListener {
                if (passwordInputLayout.isValid() and identifierInputLayout.isValid()) {
                    //findNavController().navigate(R.id.action_loginFragment_to_productListFragment)
                    view.hideKeyboard()
                    sendLoginRequest()
                }
            }
            createAccountButton.setOnClickListener {
                findNavController().navigate(R.id.loginToSignUp)
            }
        }
        requireActivity().title = getString(R.string.log_in)
    }

    private fun sendLoginRequest() {
        val identifier = binding.identifierInputLayout.editText!!.text.toString()
        val password = binding.passwordInputLayout.editText!!.text.toString()

        val request = LoginRequest(identifier, password)

        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when (response.code()) {
                    200 -> onLoginSuccess(response.body()!!)
                    in 400..499 -> onClientError(response.errorBody())
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Snackbar.make(
                    requireView(),
                    R.string.check_your_connection,
                    LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        sendLoginRequest()
                    }
                    .show()
            }
        })
    }

    private fun onUnexpectedError() {
        Snackbar.make(requireView(), R.string.unexpected_error_occured, LENGTH_LONG)
    }

    private fun onLoginSuccess(response: LoginResponse) {
        JwtStore(requireContext()).saveJwt(response.jwt)
        findNavController().navigate(R.id.loginToProductList)
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
        val errorBody = string()
        val gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }

    private fun TextInputLayout.isValid(): Boolean {
        val errorMessage = validator().validate(editText!!.text.toString())
        error = errorMessage?.resolveAsString()
        isErrorEnabled = errorMessage != null
        return errorMessage == null
    }

    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when (this) {
        binding.identifierInputLayout -> identifierValidator
        binding.passwordInputLayout -> passwordValidator
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