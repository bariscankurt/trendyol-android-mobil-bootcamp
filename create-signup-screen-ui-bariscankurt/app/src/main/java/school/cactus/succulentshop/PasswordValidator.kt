package school.cactus.succulentshop

import school.cactus.succulentshop.helpers.oneDigitOneSpecialOneLowerOneUpper

class PasswordValidator : Validator {

    override fun validateLogin(field: String): Int? = when{
        field.isEmpty() -> R.string.password_is_required
        else -> null
    }

    override fun validateSingIn(field: String): Int? = when {
        field.isEmpty() -> R.string.password_is_required
        field.length <= 7 -> R.string.password_is_too_short
        field.length >= 40 -> R.string.password_is_too_long
        field.oneDigitOneSpecialOneLowerOneUpper().not() -> R.string.password_must_contains
        else -> null
    }


}