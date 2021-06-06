package school.cactus.succulentshop.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.signup.validation.SignupEmailValidator
import school.cactus.succulentshop.signup.validation.SignupPasswordValidator
import school.cactus.succulentshop.signup.validation.SignupUsernameValidator

class SignupViewModel(
    private val store: JwtStore,
    private val repository: SignupRepository
) : BaseViewModel() {
    private val emailValidator = SignupEmailValidator()
    private val passwordValidator = SignupPasswordValidator()
    private val usernameValidator = SignupUsernameValidator()

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    private val _emailErrorMessage = MutableLiveData<Int>()
    private val _passwordErrorMessage = MutableLiveData<Int>()
    private val _usernameErrorMessage = MutableLiveData<Int>()

    val emailErrorMessage: LiveData<Int> = _emailErrorMessage
    val passwordErrorMessage: LiveData<Int> = _passwordErrorMessage
    val usernameErrorMessage: LiveData<Int> = _usernameErrorMessage


    fun onSignupButtonClick() {
        if (isEmailValid() and isPasswordValid() and isUsernameValid()) {
            repository.sendSignupRequest(
                email = email.value.orEmpty(),
                password = password.value.orEmpty(),
                username = username.value.orEmpty(),
                callback = object : GenericRequestCallback {
                    override fun <T> onSuccess(jwt: T) {
                        store.saveJwt(jwt as String)
                        val directions = SignupFragmentDirections.signupToProductList()
                        navigation.navigate(directions)
                    }

                    override fun onClientError(errorMessage: String?) {
                        _snackbarState.value = SnackbarState(
                            error = errorMessage,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                    }

                    override fun onUnexpectedError() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.unexpected_error_occurred,
                            duration = BaseTransientBottomBar.LENGTH_LONG
                        )
                    }

                    override fun onFailure() {
                        _snackbarState.value = SnackbarState(
                            errorRes = R.string.check_your_connection,
                            duration = BaseTransientBottomBar.LENGTH_INDEFINITE,
                            action = SnackbarAction(
                                text = R.string.retry,
                                action = {
                                    onSignupButtonClick()
                                }
                            )
                        )
                    }
                }
            )
        }
    }

    private fun isEmailValid(): Boolean {
        _emailErrorMessage.value = emailValidator.validate(email.value.orEmpty())
        return _emailErrorMessage.value == null
    }

    private fun isPasswordValid(): Boolean {
        _passwordErrorMessage.value = passwordValidator.validate(password.value.orEmpty())
        return _passwordErrorMessage.value == null
    }

    private fun isUsernameValid(): Boolean {
        _usernameErrorMessage.value = usernameValidator.validate(username.value.orEmpty())
        return _usernameErrorMessage.value == null
    }

}