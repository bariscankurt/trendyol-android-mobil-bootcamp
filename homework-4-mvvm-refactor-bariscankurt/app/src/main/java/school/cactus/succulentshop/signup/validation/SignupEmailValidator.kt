package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class SignupEmailValidator : Validator {
    override fun validate(field: String): Int? = when {
        field.isEmpty() -> R.string.email_is_required
        field.length <= 5 || field.length >= 50 -> R.string.email_is_invalid
        field.contains("@", ignoreCase = true).not() -> R.string.email_is_invalid
        else -> null
    }
}