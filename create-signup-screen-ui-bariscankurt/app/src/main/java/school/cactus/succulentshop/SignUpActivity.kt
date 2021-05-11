package school.cactus.succulentshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import school.cactus.succulentshop.databinding.ActivitySignUpBinding
import java.lang.IllegalArgumentException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    val emailValidator = EmailValidator()
    val passwordValidator = PasswordValidator()
    val usernameValidator = UsernameValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            signUpButton.setOnClickListener{
                binding.emailInputLayout.validate()
                binding.usernameInputLayout.validate()
                binding.passwordInputLayout.validate()
            }
            logInButton.setOnClickListener{
                navigateToLogInActivity()
            }
        }
    }

    private fun navigateToLogInActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun TextInputLayout.validate(){
        val errorMessage = this.validator().validateSingIn(this.editText!!.text.toString())
        this.error = errorMessage?.resolveAsString()
        this.isErrorEnabled = errorMessage != null
    }





    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validator() = when(this){
        binding.emailInputLayout -> emailValidator
        binding.passwordInputLayout -> passwordValidator
        binding.usernameInputLayout -> usernameValidator
        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }
}